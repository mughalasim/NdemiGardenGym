package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import cv.data.handleError
import cv.data.mappers.PaymentMapper
import cv.data.models.PaymentModel
import cv.data.toDomainError
import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.entities.PaymentYearEntity
import cv.domain.enums.AppLogType
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentRepositoryImp(
    private val pathPayment: String,
    private val pathPaymentPlan: String,
    private val firebaseAuth: FirebaseAuth,
    private val logger: AppLoggerRepository,
    private val paymentMapper: PaymentMapper,
    private val firebaseFirestore: FirebaseFirestore,
    private val dateProviderRepository: DateProviderRepository,
) : PaymentRepository {
    override fun getAllPayments(year: Int): Flow<DomainResult<PaymentYearEntity>> =
        callbackFlow {
            val reference =
                firebaseFirestore
                    .collection(pathPayment)
                    .document(pathPaymentPlan)
                    .collection(year.toString())

            val subscription =
                reference.addSnapshotListener { document, error ->
                    document?.let {
                        val response = document.toObjects<PaymentModel>()
                        val list =
                            response
                                .map { paymentMapper.getEntity(it) }
                                .sortedByDescending { it.startDateMillis }

                        var totalAmount = 0.0
                        list.forEach {
                            totalAmount += it.amount
                            logger.log("Payment data received: $it")
                        }
                        trySend(
                            DomainResult.Success(
                                PaymentYearEntity(
                                    payments = list,
                                    canAddNewPayment = false,
                                    totalAmount = totalAmount,
                                ),
                            ),
                        )
                    }
                    error?.let {
                        logger.log("Exception payment fetch: $it", AppLogType.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override fun getPayments(
        memberId: String,
        year: Int,
    ): Flow<DomainResult<PaymentYearEntity>> =
        callbackFlow {
            val setMemberId =
                memberId.ifEmpty {
                    firebaseAuth.currentUser?.uid ?: run {
                        logger.log("Not Authorised", AppLogType.ERROR)
                        trySend(DomainResult.Error(DomainErrorType.UNAUTHORISED))
                    }
                }

            val reference =
                firebaseFirestore
                    .collection(pathPayment)
                    .document(pathPaymentPlan)
                    .collection(year.toString())
                    .whereEqualTo("memberId", setMemberId)

            val subscription =
                reference.addSnapshotListener { document, error ->
                    document?.let {
                        val response = document.toObjects<PaymentModel>()
                        val list =
                            response
                                .map { paymentMapper.getEntity(it) }
                                .sortedByDescending { it.startDateMillis }

                        var canAddPayment = memberId.isNotEmpty() && dateProviderRepository.getYear() == year
                        var totalAmount = 0.0
                        list.forEach {
                            totalAmount += it.amount
                            if (dateProviderRepository.isAfterNow(it.endDateMillis)) {
                                canAddPayment = false
                            }
                            logger.log("Payment data received: $it")
                        }
                        trySend(
                            DomainResult.Success(
                                PaymentYearEntity(
                                    payments = list,
                                    canAddNewPayment = canAddPayment,
                                    totalAmount = totalAmount,
                                ),
                            ),
                        )
                    }
                    error?.let {
                        logger.log("Exception payment fetch: $it", AppLogType.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun addPaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit> =
        runCatching {
            val paymentModel = paymentMapper.getModel(paymentEntity)
            firebaseFirestore
                .collection(pathPayment)
                .document(pathPaymentPlan)
                .collection(dateProviderRepository.getYear(paymentEntity.startDateMillis).toString())
                .document(paymentModel.paymentId)
                .set(paymentModel)
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun deletePaymentPlan(
        startYear: String,
        paymentId: String,
    ): DomainResult<Unit> =
        runCatching {
            firebaseFirestore
                .collection(pathPayment)
                .document(pathPaymentPlan)
                .collection(startYear)
                .document(paymentId)
                .delete()
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )
}

package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import cv.data.mappers.toPaymentEntity
import cv.data.mappers.toPaymentModel
import cv.data.models.PaymentModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.CompletableDeferred
import org.joda.time.DateTime

class PaymentRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val pathPayment: String,
    private val pathPaymentPlan: String,
    private val logger: AppLoggerRepository,
) : PaymentRepository {
    override suspend fun getPayments(
        isMembersPayment: Boolean,
        memberId: String,
        year: Int,
    ): DomainResult<Triple<List<PaymentEntity>, Boolean, Double>> {
        val setMemberId =
            memberId.ifEmpty {
                firebaseAuth.currentUser?.uid ?: run {
                    logger.log("Not Authorised", AppLogLevel.ERROR)
                    return DomainResult.Error(DomainError.UNAUTHORISED)
                }
            }

        val reference =
            firebaseFirestore
                .collection(pathPayment)
                .document(pathPaymentPlan)
                .collection(year.toString())

        val completable:
            CompletableDeferred<DomainResult<Triple<List<PaymentEntity>, Boolean, Double>>> =
            CompletableDeferred()

        reference.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<PaymentModel>()
                val list =
                    response
                        .map { it.toPaymentEntity() }
                        .sortedByDescending { it.startDateMillis }
                        .filter { it.memberId == setMemberId && isMembersPayment }

                var canAddPayment = memberId.isNotEmpty() && DateTime.now().year == year
                var totalAmount = 0.0
                list.forEach {
                    totalAmount += it.amount
                    if (DateTime(it.endDateMillis).isAfterNow) {
                        canAddPayment = false
                    }
                }
                completable.complete(DomainResult.Success(Triple(list, canAddPayment, totalAmount)))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun addPaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit> {
        val paymentModel = paymentEntity.toPaymentModel()

        val collection =
            firebaseFirestore
                .collection(pathPayment)
                .document(pathPaymentPlan)
                .collection(DateTime(paymentEntity.startDateMillis).year.toString())
                .document(paymentModel.paymentId)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.set(paymentModel)
            .addOnSuccessListener {
                logger.log("Payment Plan Added")
                completable.complete(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deletePaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit> {
        val collection =
            firebaseFirestore
                .collection(pathPayment)
                .document(pathPaymentPlan)
                .collection(DateTime(paymentEntity.startDateMillis).year.toString())
                .document(paymentEntity.paymentId)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Payment Plan Deleted")
                completable.complete(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }
}

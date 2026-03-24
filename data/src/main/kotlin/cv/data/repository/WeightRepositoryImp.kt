package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import cv.data.handleError
import cv.data.mappers.WeightMapper
import cv.data.models.WeightModel
import cv.data.toDomainError
import cv.domain.DomainResult
import cv.domain.entities.WeightEntity
import cv.domain.enums.AppLogType
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.WeightRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WeightRepositoryImp(
    private val pathWeight: String,
    private val pathTracked: String,
    private val firebaseAuth: FirebaseAuth,
    private val logger: AppLoggerRepository,
    private val weightMapper: WeightMapper,
    private val firebaseFirestore: FirebaseFirestore,
) : WeightRepository {
    override fun getWeightForYear(year: Int): Flow<DomainResult<List<WeightEntity>>> =
        callbackFlow {
            val memberId = firebaseAuth.currentUser?.uid ?: ""
            val reference =
                firebaseFirestore
                    .collection(pathWeight)
                    .document(pathTracked)
                    .collection(memberId)
                    .whereEqualTo("year", year)

            val subscription =
                reference.addSnapshotListener { document, error ->
                    document?.let {
                        val response = document.toObjects<WeightModel>()
                        val weightList = response.map { weightMapper.getEntity(it) }.sortedByDescending { it.dateMillis }
                        logger.log("Weight data received: $weightList")
                        trySend(DomainResult.Success(weightList))
                    }
                    error?.let {
                        logger.log("Exception weight fetch: $it", AppLogType.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun setWeight(weightEntity: WeightEntity): DomainResult<Unit> =
        runCatching {
            val weightModel = weightMapper.getModel(weightEntity)
            val memberId = firebaseAuth.currentUser?.uid ?: ""
            firebaseFirestore
                .collection(pathWeight)
                .document(pathTracked)
                .collection(memberId)
                .document(weightModel.id)
                .set(weightModel)
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun deleteWeight(weightId: String): DomainResult<Unit> =
        runCatching {
            val memberId = firebaseAuth.currentUser?.uid ?: ""
            firebaseFirestore
                .collection(pathWeight)
                .document(pathTracked)
                .collection(memberId)
                .document(weightId)
                .delete()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )
}

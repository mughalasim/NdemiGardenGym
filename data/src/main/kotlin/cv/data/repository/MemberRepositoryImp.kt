package cv.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import cv.data.handleError
import cv.data.mappers.toMemberEntity
import cv.data.mappers.toMemberModel
import cv.data.models.MemberModel
import cv.data.toDomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.AppLogType
import cv.domain.enums.DomainErrorType
import cv.domain.enums.MemberFetchType
import cv.domain.enums.MemberType
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MemberRepositoryImp(
    private val firebaseFirestore: FirebaseFirestore,
    private val pathUser: String,
    private val logger: AppLoggerRepository,
) : MemberRepository {
    override suspend fun getMemberById(memberId: String): DomainResult<MemberEntity> =
        runCatching {
            firebaseFirestore.collection(pathUser).document(memberId).get().await()
        }.fold(
            onSuccess = { result ->
                logger.log("Member by ID: ${result.toObject<Any>()}")
                val response = result.toObject<MemberModel>()
                return response?.let {
                    DomainResult.Success(it.toMemberEntity())
                } ?: run {
                    DomainResult.Error(DomainErrorType.NO_DATA)
                }
            },
            onFailure = { handleError(it, logger) },
        )

    override fun getMembers(fetchType: MemberFetchType): Flow<DomainResult<List<MemberEntity>>> =
        callbackFlow {
            val query =
                when (fetchType) {
                    MemberFetchType.ALL ->
                        firebaseFirestore.collection(pathUser)
                            .whereNotEqualTo("memberType", MemberType.SUPER_ADMIN)

                    MemberFetchType.NON_MEMBERS ->
                        firebaseFirestore.collection(pathUser)
                            .whereNotEqualTo("memberType", MemberType.MEMBER)

                    else ->
                        firebaseFirestore.collection(pathUser)
                            .whereEqualTo("memberType", MemberType.MEMBER)
                }

            val subscription =
                query.addSnapshotListener { snapshot, error ->
                    snapshot?.let { querySnapshot ->
                        logger.log("Members list: ${querySnapshot.toObjects<Any>()}")
                        val response =
                            querySnapshot
                                .toObjects<MemberModel>()
                                .map { it.toMemberEntity() }
                        trySend(
                            DomainResult.Success(
                                when (fetchType) {
                                    MemberFetchType.ALL, MemberFetchType.NON_MEMBERS ->
                                        response.sortedByDescending { it.registrationDateMillis }

                                    MemberFetchType.MEMBERS ->
                                        response
                                            .filter { it.renewalFutureDateMillis != null }
                                            .sortedBy { it.renewalFutureDateMillis }

                                    MemberFetchType.ACTIVE ->
                                        response
                                            .filter { it.activeNowDateMillis != null }
                                            .sortedBy { it.activeNowDateMillis }

                                    MemberFetchType.EXPIRED_REGISTRATIONS ->
                                        response
                                            .filter { it.renewalFutureDateMillis == null }
                                            .sortedByDescending { it.registrationDateMillis }
                                },
                            ),
                        )
                    }
                    error?.let {
                        logger.log("Exception members list: $it", AppLogType.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Unit> =
        runCatching {
            val memberModel = memberEntity.toMemberModel()
            firebaseFirestore
                .collection(pathUser)
                .document(memberModel.id)
                .set(memberModel)
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit> =
        runCatching {
            val memberModel = memberEntity.toMemberModel()
            firebaseFirestore
                .collection(pathUser)
                .document(memberModel.id)
                .delete()
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )
}

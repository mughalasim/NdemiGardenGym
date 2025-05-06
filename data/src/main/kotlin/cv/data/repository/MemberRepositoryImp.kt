package cv.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import cv.data.mappers.toMemberEntity
import cv.data.mappers.toMemberModel
import cv.data.models.MemberModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.entities.MemberType
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MemberRepositoryImp(
    private val firebaseFirestore: FirebaseFirestore,
    private val pathUser: String,
    private val logger: AppLoggerRepository,
) : MemberRepository {
    override suspend fun getMemberById(memberId: String): DomainResult<MemberEntity> {
        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        firebaseFirestore.collection(pathUser).document(memberId).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: ${document.toObject<Any>()}")
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getAllMembers(isActiveNow: Boolean): Flow<DomainResult<List<MemberEntity>>> =
        callbackFlow {
            val query = firebaseFirestore.collection(pathUser).whereEqualTo("memberType", MemberType.MEMBER)
            if (isActiveNow) {
                query.whereNotEqualTo("activeNowDate", null)
            }

            val subscription =
                query.addSnapshotListener { snapshot, error ->
                    snapshot?.let { querySnapshot ->
                        logger.log("Data received: ${querySnapshot.toObjects<Any>()}")
                        val response = querySnapshot.toObjects<MemberModel>()
                        trySend(
                            DomainResult.Success(
                                response.map {
                                    it.toMemberEntity()
                                }.filter {
                                    if (isActiveNow) {
                                        it.activeNowDateMillis != null
                                    } else {
                                        it.hasPaidMembership()
                                    }
                                }.sortedByDescending {
                                    it.registrationDateMillis
                                },
                            ),
                        )
                    }
                    error?.let {
                        logger.log("Exception: $it", AppLogLevel.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun getExpiredMembers(): Flow<DomainResult<List<MemberEntity>>> =
        callbackFlow {
            val collection = firebaseFirestore.collection(pathUser).whereEqualTo("memberType", MemberType.MEMBER)
            val subscription =
                collection.addSnapshotListener { snapshot, error ->
                    snapshot?.let {
                        logger.log("Data received: ${it.toObjects<Any>()}")
                        val response = it.toObjects<MemberModel>()
                        trySend(
                            DomainResult.Success(
                                response.map {
                                    it.toMemberEntity()
                                }.filter {
                                    !it.hasPaidMembership()
                                }.sortedByDescending {
                                    it.renewalFutureDateMillis
                                },
                            ),
                        )
                    }
                    error?.let {
                        logger.log("Exception: $it", AppLogLevel.ERROR)
                        trySend(DomainResult.Error(it.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean> {
        val completable: CompletableDeferred<DomainResult<Boolean>> = CompletableDeferred()
        val memberModel = memberEntity.toMemberModel()

        firebaseFirestore.collection(pathUser).document(memberModel.id).set(memberModel)
            .addOnSuccessListener {
                completable.complete(DomainResult.Success(true))
            }
            .addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit> {
        val memberModel = memberEntity.toMemberModel()

        val collection =
            firebaseFirestore
                .collection(pathUser)
                .document(memberModel.id)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Member Deleted")
                completable.complete(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }
}

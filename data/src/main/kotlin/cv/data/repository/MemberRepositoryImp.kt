package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
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

class MemberRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val pathUser: String,
    private val logger: AppLoggerRepository,
) : MemberRepository {

    override suspend fun getMember(): DomainResult<MemberEntity> {
        val id = firebaseAuth.currentUser?.uid ?: run {
            logger.log("Not Authorised", AppLogLevel.ERROR)
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }

        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        firebaseFirestore.collection(pathUser).document(id).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
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

    override suspend fun getMemberById(
        memberId: String
    ): DomainResult<MemberEntity> {
        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        firebaseFirestore.collection(pathUser).document(memberId).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
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

    override suspend fun getAllMembers(
        isActiveNow: Boolean
    ): DomainResult<List<MemberEntity>> {
        val completable: CompletableDeferred<DomainResult<List<MemberEntity>>> =
            CompletableDeferred()
        val collection = firebaseFirestore.collection(pathUser)

        collection.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<MemberModel>()
                completable.complete(DomainResult.Success(
                    response.map {
                        it.toMemberEntity()
                    }.filter {
                        if (isActiveNow) {
                            it.activeNowDateMillis != null
                        } else {
                            it.memberType == MemberType.MEMBER && it.hasPaidMembership()
                        }
                    }.sortedByDescending {
                        it.registrationDateMillis
                    })
                )

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getExpiredMembers(): DomainResult<List<MemberEntity>> {
        val completable: CompletableDeferred<DomainResult<List<MemberEntity>>> =
            CompletableDeferred()
        val collection = firebaseFirestore.collection(pathUser)

        collection.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<MemberModel>()
                completable.complete(DomainResult.Success(
                    response.map {
                        it.toMemberEntity()
                    }.filter {
                        !it.hasPaidMembership() && it.memberType == MemberType.MEMBER
                    }.sortedByDescending {
                        it.renewalFutureDateMillis
                    })
                )

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun updateMember(
        memberEntity: MemberEntity
    ): DomainResult<Boolean> {
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

    override suspend fun deleteMember(
        memberEntity: MemberEntity
    ): DomainResult<Unit> {
        val memberModel = memberEntity.toMemberModel()

        val collection = firebaseFirestore
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

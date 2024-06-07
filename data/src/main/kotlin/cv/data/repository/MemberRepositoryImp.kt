package cv.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import cv.data.Variables.PATH_ATTENDANCE
import cv.data.Variables.PATH_USER
import cv.data.models.AttendanceModel
import cv.data.models.MemberModel
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.CompletableDeferred

class MemberRepositoryImp(
    private val logger: AppLoggerRepository,
) : MemberRepository {
    private val database = Firebase.firestore

    override suspend fun getMember(memberId: String): DomainResult<MemberEntity> {
        val id = memberId.ifEmpty {
            Firebase.auth.currentUser?.uid ?: run {
                return DomainResult.Error(DomainError.UNAUTHORISED)
            }
        }
        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        database.collection(PATH_USER).document(id).get()
            .addOnSuccessListener { document ->
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }

            }.addOnFailureListener {
                completable.complete(DomainResult.Error(DomainError.UNAUTHORISED))
            }
        return completable.await()
    }

    override suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>> {
        val completable: CompletableDeferred<DomainResult<List<MemberEntity>>> =
            CompletableDeferred()
        val collection = database.collection(PATH_USER)
        if (isLive) collection.where(Filter.notEqualTo("activeNowDate", null))
        collection.get()
            .addOnSuccessListener { document ->
                val response = document.toObjects<MemberModel>()
                completable.complete(DomainResult.Success(response.map { it.toMemberEntity() }))
            }.addOnFailureListener {
                completable.complete(DomainResult.Error(DomainError.UNAUTHORISED))
            }
        return completable.await()
    }

    override suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean> {
        val completable: CompletableDeferred<DomainResult<Boolean>> = CompletableDeferred()
        database.collection(PATH_USER).document(memberEntity.id).set(memberEntity)
            .addOnSuccessListener {
                completable.complete(DomainResult.Success(true))
            }
            .addOnFailureListener {
                completable.complete(DomainResult.Success(false))
            }
        return completable.await()
    }

    override suspend fun getAttendances(
        isMembersAttendances: Boolean,
        year: Int,
        month: Int
    ): DomainResult<List<AttendanceEntity>> {
        val memberId = Firebase.auth.currentUser?.uid ?: run {
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }
        val collection = database.collection(PATH_ATTENDANCE)
            .document(year.toString()).collection(month.toString())

        if (isMembersAttendances) {
            collection.where(Filter.equalTo("memberId", memberId))
        }

        val completable: CompletableDeferred<DomainResult<List<AttendanceEntity>>> =
            CompletableDeferred()
        collection.get()
            .addOnSuccessListener { document ->
                val response = document.toObjects<AttendanceModel>()
                completable.complete(DomainResult.Success(response.map { it.toAttendanceEntity() }))

            }.addOnFailureListener {
                completable.complete(DomainResult.Error(DomainError.UNAUTHORISED))
            }
        return completable.await()
    }

}

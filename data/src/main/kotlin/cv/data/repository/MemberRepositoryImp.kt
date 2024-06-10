package cv.data.repository

import com.google.firebase.Timestamp
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
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.CompletableDeferred
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Minutes
import java.util.Date

class MemberRepositoryImp(
    private val logger: AppLoggerRepository,
) : MemberRepository {
    private val database = Firebase.firestore

    override suspend fun getMember(): DomainResult<MemberEntity> {
        val id = Firebase.auth.currentUser?.uid ?: run {
            logger.log("Not Authorised")
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }

        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        database.collection(PATH_USER).document(id).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getMember(memeberId: String): DomainResult<MemberEntity> {
        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        database.collection(PATH_USER).document(memeberId).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>> {
        val completable: CompletableDeferred<DomainResult<List<MemberEntity>>> =
            CompletableDeferred()
        val collection = database.collection(PATH_USER)

        collection.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<MemberModel>()
                if (isLive) {
                    completable.complete(DomainResult.Success(
                        response.filter { it.activeNowDate != null }.map { it.toMemberEntity() })
                    )
                } else {
                    completable.complete(DomainResult.Success(
                        response.map { it.toMemberEntity() })
                    )
                }

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean> {
        val completable: CompletableDeferred<DomainResult<Boolean>> = CompletableDeferred()
        val memberModel = memberEntity.toMemberModel()

        database.collection(PATH_USER).document(memberModel.id).set(memberModel)
            .addOnSuccessListener {
                completable.complete(DomainResult.Success(true))
            }

            .addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
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
                logger.log("Data received: $document")
                val response = document.toObjects<AttendanceModel>()
                completable.complete(DomainResult.Success(response.map { it.toAttendanceEntity() }))

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun addAttendance(startDate: Date, endDate: Date): DomainResult<Unit> {
        val memberId = Firebase.auth.currentUser?.uid ?: run {
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }

        val startDateTime = DateTime(startDate)
        val endDateTime = DateTime(endDate)

        if (endDateTime.isBefore(startDateTime) ||
            Days.daysBetween(startDateTime, endDateTime).days > 1 ||
            Minutes.minutesBetween(startDateTime, endDateTime).minutes < 1
        ) {
            return DomainResult.Error(DomainError.INVALID_SESSION_TIME)
        }

        val attendanceModel = AttendanceModel(
            memberId = memberId,
            startDate = Timestamp(startDate),
            endDate = Timestamp(endDate)
        )
        val collection = database
            .collection(PATH_ATTENDANCE)
            .document(startDateTime.year.toString())
            .collection(startDateTime.monthOfYear.toString())
            .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.set(attendanceModel)
            .addOnSuccessListener {
                logger.log("Attendance Added")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> {
        val attendanceModel = attendanceEntity.toAttendanceModel()
        val startDateTime = DateTime(attendanceEntity.startDate)

        val collection = database
            .collection(PATH_ATTENDANCE)
            .document(startDateTime.year.toString())
            .collection(startDateTime.monthOfYear.toString())
            .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Attendance Deleted")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it")
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    private fun AttendanceEntity.toAttendanceModel() = AttendanceModel(
        memberId = memberId,
        startDate = Timestamp(startDate),
        endDate = Timestamp(endDate)
    )

    private fun MemberEntity.toMemberModel() = MemberModel(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        activeNowDate = activeNowDate?.let { Timestamp(it) }?: run { null },
        renewalFutureDate = renewalFutureDate?.let { Timestamp(it) }?: run { null },
        registrationDate = Timestamp(registrationDate),
    )

}

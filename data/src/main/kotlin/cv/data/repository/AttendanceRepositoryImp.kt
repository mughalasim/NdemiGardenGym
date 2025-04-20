package cv.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import cv.data.mappers.toAttendanceEntity
import cv.data.mappers.toAttendanceModel
import cv.data.models.AttendanceModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AttendanceRepository
import kotlinx.coroutines.CompletableDeferred
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Minutes
import java.util.Date

class AttendanceRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val pathAttendance: String,
    private val logger: AppLoggerRepository,
) : AttendanceRepository {
    override suspend fun getAttendances(
        isMembersAttendances: Boolean,
        memberId: String,
        year: Int,
        month: Int,
    ): DomainResult<Pair<List<AttendanceEntity>, Int>> {
        val setMemberId =
            memberId.ifEmpty {
                firebaseAuth.currentUser?.uid ?: run {
                    logger.log("Not Authorised", AppLogLevel.ERROR)
                    return DomainResult.Error(DomainError.UNAUTHORISED)
                }
            }
        val reference =
            firebaseFirestore
                .collection(pathAttendance)
                .document(year.toString())
                .collection(month.toString())

        val completable: CompletableDeferred<DomainResult<Pair<List<AttendanceEntity>, Int>>> =
            CompletableDeferred()
        reference.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<AttendanceModel>()
                val list =
                    response
                        .map { it.toAttendanceEntity() }
                        .sortedByDescending { it.startDateMillis }
                        .filter { it.memberId == setMemberId && isMembersAttendances }

                var totalMinutes = 0
                list.forEach {
                    totalMinutes +=
                        Minutes.minutesBetween(
                            DateTime(it.startDateMillis),
                            DateTime(it.endDateMillis),
                        ).minutes
                }
                completable.complete(
                    DomainResult.Success(Pair(list, totalMinutes)),
                )
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ): DomainResult<Unit> {
        val setMemberId =
            memberId.ifEmpty {
                firebaseAuth.currentUser?.uid ?: run {
                    logger.log("Not Authorised", AppLogLevel.ERROR)
                    return DomainResult.Error(DomainError.UNAUTHORISED)
                }
            }

        val startDateTime = DateTime(startDate)
        val endDateTime = DateTime(endDate)

        if (endDateTime.isBefore(startDateTime) ||
            Days.daysBetween(startDateTime, endDateTime).days > 1 ||
            Minutes.minutesBetween(startDateTime, endDateTime).minutes < 1
        ) {
            return DomainResult.Error(DomainError.INVALID_SESSION_TIME)
        }

        val attendanceModel =
            AttendanceModel(
                memberId = setMemberId,
                startDate = Timestamp(startDate),
                endDate = Timestamp(endDate),
            )
        val collection =
            firebaseFirestore
                .collection(pathAttendance)
                .document(startDateTime.year.toString())
                .collection(startDateTime.monthOfYear.toString())
                .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.set(attendanceModel)
            .addOnSuccessListener {
                logger.log("Attendance Added")
                completable.complete(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> {
        val attendanceModel = attendanceEntity.toAttendanceModel()
        val startDateTime = DateTime(attendanceEntity.startDateMillis)

        val collection =
            firebaseFirestore
                .collection(pathAttendance)
                .document(startDateTime.year.toString())
                .collection(startDateTime.monthOfYear.toString())
                .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Attendance Deleted")
                completable.complete(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }
}

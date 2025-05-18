package cv.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.toObjects
import cv.data.handleError
import cv.data.mappers.toAttendanceEntity
import cv.data.mappers.toAttendanceModel
import cv.data.models.AttendanceModel
import cv.data.toDomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.enums.AppLogType
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AttendanceRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
    override fun getAttendances(
        memberId: String,
        year: Int,
        month: Int,
    ): Flow<DomainResult<AttendanceMonthEntity>> =
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
                    .collection(pathAttendance)
                    .document(year.toString())
                    .collection(month.toString()).whereEqualTo("memberId", setMemberId)

            val subscription =
                reference
                    .addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            val response = snapshot.toObjects<AttendanceModel>()
                            val list =
                                response
                                    .map { it.toAttendanceEntity() }
                                    .sortedByDescending { it.startDateMillis }

                            var totalMinutes = 0
                            list.forEach {
                                totalMinutes +=
                                    Minutes.minutesBetween(
                                        DateTime(it.startDateMillis),
                                        DateTime(it.endDateMillis),
                                    ).minutes
                                logger.log("Attendance received: $it")
                            }
                            trySend(
                                DomainResult.Success(
                                    AttendanceMonthEntity(
                                        monthNumber = month,
                                        totalMinutes = totalMinutes,
                                        attendances = list,
                                    ),
                                ),
                            )
                        }
                        error?.let {
                            logger.log("Exception attendance: $it", AppLogType.ERROR)
                            trySend(DomainResult.Error(it.toDomainError()))
                        }
                    }
            awaitClose { subscription.remove() }
        }

    override suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ): DomainResult<Unit> =
        runCatching {
            val setMemberId =
                memberId.ifEmpty {
                    firebaseAuth.currentUser?.uid ?: run {
                        logger.log("Not authorised", AppLogType.ERROR)
                        throw FirebaseFirestoreException("Not authorised", Code.UNAUTHENTICATED)
                    }
                }

            val startDateTime = DateTime(startDate)
            val endDateTime = DateTime(endDate)

            if (endDateTime.isBefore(startDateTime) ||
                Days.daysBetween(startDateTime, endDateTime).days > 1 ||
                Minutes.minutesBetween(startDateTime, endDateTime).minutes < 1
            ) {
                logger.log("Invalid argument passed", AppLogType.ERROR)
                throw FirebaseFirestoreException("Invalid argument passed", Code.FAILED_PRECONDITION)
            }

            val attendanceModel =
                AttendanceModel(
                    memberId = setMemberId,
                    startDate = Timestamp(startDate),
                    endDate = Timestamp(endDate),
                )
            firebaseFirestore
                .collection(pathAttendance)
                .document(startDateTime.year.toString())
                .collection(startDateTime.monthOfYear.toString())
                .document(attendanceModel.getAttendanceId())
                .set(attendanceModel)
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> =
        runCatching {
            val startDateTime = DateTime(attendanceEntity.startDateMillis)
            firebaseFirestore
                .collection(pathAttendance)
                .document(startDateTime.year.toString())
                .collection(startDateTime.monthOfYear.toString())
                .document(attendanceEntity.toAttendanceModel().getAttendanceId())
                .delete()
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )
}

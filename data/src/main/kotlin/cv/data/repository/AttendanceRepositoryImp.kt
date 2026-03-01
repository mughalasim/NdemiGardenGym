package cv.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.toObjects
import cv.data.handleError
import cv.data.mappers.AttendanceMapper
import cv.data.models.AttendanceModel
import cv.data.toDomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.enums.AppLogType
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.DateProviderRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class AttendanceRepositoryImp(
    private val pathAttendance: String,
    private val firebaseAuth: FirebaseAuth,
    private val logger: AppLoggerRepository,
    private val attendanceMapper: AttendanceMapper,
    private val firebaseFirestore: FirebaseFirestore,
    private val dateProviderRepository: DateProviderRepository,
) : AttendanceRepository {
    override fun getAllAttendances(
        year: Int,
        month: Int,
    ): Flow<DomainResult<AttendanceMonthEntity>> =
        callbackFlow {
            val reference =
                firebaseFirestore
                    .collection(pathAttendance)
                    .document(year.toString())
                    .collection(month.toString())

            val subscription =
                reference
                    .addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            val response = snapshot.toObjects<AttendanceModel>()
                            val list =
                                response.map { attendanceMapper.getEntity(it) }

                            var totalMinutes = 0
                            list.forEach {
                                totalMinutes +=
                                    dateProviderRepository.minutesBetween(
                                        startDateMillis = it.startDateMillis,
                                        endDateMillis = it.endDateMillis,
                                    )
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
                    .collection(month.toString())
                    .whereEqualTo("memberId", setMemberId)

            val subscription =
                reference
                    .addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            val response = snapshot.toObjects<AttendanceModel>()
                            val list =
                                response
                                    .map { attendanceMapper.getEntity(it) }
                                    .sortedByDescending { it.startDateMillis }

                            var totalMinutes = 0
                            list.forEach {
                                totalMinutes +=
                                    dateProviderRepository.minutesBetween(
                                        startDateMillis = it.startDateMillis,
                                        endDateMillis = it.endDateMillis,
                                    )
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

            if (dateProviderRepository.isWithinCurrentDay(startDate, endDate)) {
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
                .document(dateProviderRepository.getYear(startDate).toString())
                .collection(dateProviderRepository.getMonth(startDate).toString())
                .document(attendanceModel.getAttendanceId())
                .set(attendanceModel)
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> =
        runCatching {
            firebaseFirestore
                .collection(pathAttendance)
                .document(dateProviderRepository.getYear(attendanceEntity.startDateMillis).toString())
                .collection(dateProviderRepository.getMonth(attendanceEntity.startDateMillis).toString())
                .document(attendanceMapper.getModel(attendanceEntity).getAttendanceId())
                .delete()
                .await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )
}

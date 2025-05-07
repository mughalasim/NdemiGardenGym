package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ATTENDANCE_DELETE
import cv.domain.Variables.PARAM_ATTENDANCE_DELETE
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AttendanceRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.merge
import java.util.Date

class AttendanceUseCase(
    private val attendanceRepository: AttendanceRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    fun getMemberAttendancesForId(
        memberId: String = "",
        year: Int,
    ): Flow<DomainResult<List<AttendanceMonthEntity>>> =
        callbackFlow {
            val result: MutableList<AttendanceMonthEntity> = mutableListOf()

            (JANUARY..DECEMBER).map { month ->
                attendanceRepository.getAttendances(memberId, year, month)
            }.merge()
                .collect { response ->
                    if (response is DomainResult.Success && response.data.attendances.isNotEmpty()) {
                        result.removeIf { it.monthNumber == response.data.monthNumber }
                        result.add(
                            AttendanceMonthEntity(
                                monthNumber = response.data.monthNumber,
                                totalMinutes = response.data.totalMinutes,
                                attendances = response.data.attendances,
                            ),
                        )
                    }
                    trySend(DomainResult.Success(result.sortedBy { it.monthNumber }))
                }

            awaitClose()
        }

    suspend fun addAttendance(
        startDate: Date,
        endDate: Date,
    ) = attendanceRepository.addAttendance(
        memberId = "",
        startDate = startDate,
        endDate = endDate,
    )

    suspend fun addAttendanceForMember(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ) = attendanceRepository.addAttendance(
        memberId = memberId,
        startDate = startDate,
        endDate = endDate,
    )

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_ATTENDANCE_DELETE,
            params =
                listOf(
                    Pair(PARAM_ATTENDANCE_DELETE, attendanceEntity.memberId),
                ),
        )
        return attendanceRepository.deleteAttendance(attendanceEntity)
    }
}

private const val DECEMBER = 12
private const val JANUARY = 1

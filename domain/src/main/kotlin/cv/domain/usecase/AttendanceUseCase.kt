package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ATTENDANCE_DELETE
import cv.domain.Variables.PARAM_ATTENDANCE_DELETE
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AttendanceRepository
import java.util.Date

class AttendanceUseCase(
    private val attendanceRepository: AttendanceRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun getMemberAttendancesForId(
        memberId: String = "",
        year: Int,
    ): DomainResult<List<AttendanceMonthEntity>> {
        val result: MutableList<AttendanceMonthEntity> = mutableListOf()
        for (month in JANUARY..DECEMBER) {
            val response =
                attendanceRepository.getAttendances(
                    memberId = memberId,
                    year = year,
                    month = month,
                )
            if (response is DomainResult.Success && response.data.first.isNotEmpty()) {
                result.add(
                    AttendanceMonthEntity(
                        monthNumber = month,
                        totalMinutes = response.data.second,
                        attendances = response.data.first,
                    ),
                )
            }
        }
        return DomainResult.Success(result)
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

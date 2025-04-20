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
        month: Int,
    ): DomainResult<List<AttendanceMonthEntity>> {
        val result: MutableList<AttendanceMonthEntity> = mutableListOf()
        for (monthNumber in 1..12) {
            val response = attendanceRepository.getAttendances(
                memberId = memberId,
                year = 2024,
                month = monthNumber,
            )
            when (response) {
                is DomainResult.Success -> {
                    if (response.data.first.isNotEmpty()) {
                        result.add(
                            AttendanceMonthEntity(
                                monthName = monthNumber.toMonthName(),
                                totalMinutes = response.data.second,
                                attendances = response.data.first
                            )
                        )
                    }
                }

                else -> Unit
            }

        }

        return DomainResult.Success(result)

    }
    // TODO - Use date time correctly and removed hard coded date
    private fun Int.toMonthName() =
        when (this) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Ap"
            5 -> "Ma"
            6 -> "June"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sept"
            10 -> "Oct"
            11 -> "Nov"
            else -> "Dec"
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

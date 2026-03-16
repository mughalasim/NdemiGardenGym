package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ATTENDANCE_DELETE
import cv.domain.Variables.PARAM_ATTENDANCE_DELETE
import cv.domain.mappers.AttendancePresentationMapper
import cv.domain.presentationModels.AttendanceMonthPresentationModel
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
    private val attendancePresentationMapper: AttendancePresentationMapper,
) {
    fun getMemberAttendancesForId(
        memberId: String = "",
        year: Int,
    ): Flow<DomainResult<List<AttendanceMonthPresentationModel>>> =
        callbackFlow {
            val result: MutableList<AttendanceMonthPresentationModel> = mutableListOf()

            (JANUARY..DECEMBER)
                .map { month ->
                    attendanceRepository.getAttendances(memberId, year, month)
                }.merge()
                .collect { response ->
                    if (response is DomainResult.Success && response.data.attendances.isNotEmpty()) {
                        result.removeIf { it.monthNumber == response.data.monthNumber }
                        result.add(
                            attendancePresentationMapper.getModel(response.data),
                        )
                    }
                    trySend(DomainResult.Success(result.sortedByDescending { it.monthNumber }))
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

    suspend fun deleteAttendance(
        startYear: String,
        startMonth: String,
        attendanceId: String,
    ): DomainResult<Unit> {
        analyticsRepository.logEvent(EVENT_ATTENDANCE_DELETE, PARAM_ATTENDANCE_DELETE, attendanceId)
        return attendanceRepository.deleteAttendance(
            startYear = startYear,
            startMonth = startMonth,
            attendanceId = attendanceId,
        )
    }
}

private const val DECEMBER = 12
private const val JANUARY = 1

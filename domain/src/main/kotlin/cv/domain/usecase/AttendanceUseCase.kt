package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ACTIVE
import cv.domain.Variables.EVENT_ATTENDANCE_DELETE
import cv.domain.Variables.EVENT_MEMBERSHIP
import cv.domain.Variables.EVENT_MEMBER_DELETE
import cv.domain.Variables.EVENT_MEMBER_UPDATE
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.EVENT_REGISTRATION
import cv.domain.Variables.PARAM_ACTIVE_SESSION_FALSE
import cv.domain.Variables.PARAM_ACTIVE_SESSION_TRUE
import cv.domain.Variables.PARAM_ATTENDANCE_DELETE
import cv.domain.Variables.PARAM_MEMBERSHIP_ADDED
import cv.domain.Variables.PARAM_MEMBER_DELETE
import cv.domain.Variables.PARAM_MEMBER_UPDATE
import cv.domain.Variables.PARAM_PHOTO_DELETE
import cv.domain.Variables.PARAM_REGISTRATION_ADMIN
import cv.domain.Variables.PARAM_REGISTRATION_SELF
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.MemberRepository
import java.util.Date

class AttendanceUseCase(
    private val attendanceRepository: AttendanceRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun getMemberAttendancesForId(memberId: String, year: Int, month: Int) =
        attendanceRepository.getAttendances(
            isMembersAttendances = true,
            memberId = memberId,
            year = year,
            month = month
        )

    suspend fun getMemberAttendances(year: Int, month: Int) =
        attendanceRepository.getAttendances(
            isMembersAttendances = true,
            memberId = "",
            year = year,
            month = month
        )

    suspend fun addAttendance(startDate: Date, endDate: Date) =
        attendanceRepository.addAttendance(
            memberId = "",
            startDate = startDate,
            endDate = endDate
        )

    suspend fun addAttendanceForMember(memberId: String, startDate: Date, endDate: Date) =
        attendanceRepository.addAttendance(
            memberId = memberId,
            startDate = startDate,
            endDate = endDate
        )

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_ATTENDANCE_DELETE,
            params = listOf(
                Pair(PARAM_ATTENDANCE_DELETE, attendanceEntity.memberId)
            )
        )
        return attendanceRepository.deleteAttendance(attendanceEntity)
    }
}

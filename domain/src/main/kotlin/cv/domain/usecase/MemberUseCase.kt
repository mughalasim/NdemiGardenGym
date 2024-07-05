package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ACTIVE
import cv.domain.Variables.EVENT_ATTENDANCE_DELETE
import cv.domain.Variables.EVENT_HAS_COACH
import cv.domain.Variables.EVENT_MEMBERSHIP
import cv.domain.Variables.EVENT_MEMBER_DELETE
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.EVENT_REGISTRATION
import cv.domain.Variables.PARAM_ACTIVE_SESSION_FALSE
import cv.domain.Variables.PARAM_ACTIVE_SESSION_TRUE
import cv.domain.Variables.PARAM_ATTENDANCE_DELETE
import cv.domain.Variables.PARAM_HAS_COACH_NO
import cv.domain.Variables.PARAM_HAS_COACH_YES
import cv.domain.Variables.PARAM_MEMBERSHIP_ADDED
import cv.domain.Variables.PARAM_MEMBER_DELETE
import cv.domain.Variables.PARAM_PHOTO_DELETE
import cv.domain.Variables.PARAM_REGISTRATION_ADMIN
import cv.domain.Variables.PARAM_REGISTRATION_SELF
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository
import java.util.Date

class MemberUseCase(
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun getMember() =
        memberRepository.getMember()

    suspend fun getMemberById(memberId: String) =
        memberRepository.getMemberById(memberId = memberId)

    suspend fun getAllMembers() =
        memberRepository.getAllMembers(isLive = false)

    suspend fun getLiveMembers() =
        memberRepository.getAllMembers(isLive = true)

    suspend fun getMemberAttendances(year: Int, month: Int) =
        memberRepository.getAttendances(
            isMembersAttendances = true,
            memberId = "",
            year = year,
            month = month
        )

    suspend fun getMemberAttendancesForId(memberId: String, year: Int, month: Int) =
        memberRepository.getAttendances(
            isMembersAttendances = true,
            memberId = memberId,
            year = year,
            month = month
        )

    suspend fun addAttendance(startDate: Date, endDate: Date) =
        memberRepository.addAttendance(
            memberId = "",
            startDate = startDate,
            endDate = endDate
        )

    suspend fun addAttendanceForMember(memberId: String, startDate: Date, endDate: Date) =
        memberRepository.addAttendance(
            memberId = memberId,
            startDate = startDate,
            endDate = endDate
        )

    suspend fun updateMember(
        memberEntity: MemberEntity,
        updateType: UpdateType
    ): DomainResult<Boolean> {
        when(updateType){
            UpdateType.ADMIN_REGISTRATION -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_REGISTRATION,
                    params = listOf(
                        Pair(PARAM_REGISTRATION_ADMIN, memberEntity.id)
                    )
                )
            }
            UpdateType.SELF_REGISTRATION -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_REGISTRATION,
                    params = listOf(
                        Pair(PARAM_REGISTRATION_SELF, memberEntity.id)
                    )
                )
            }
            UpdateType.MEMBERSHIP -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_MEMBERSHIP,
                    params = listOf(
                        Pair(PARAM_MEMBERSHIP_ADDED, memberEntity.id)
                    )
                )
            }

            UpdateType.PHOTO_DELETE -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_PHOTO,
                    params = listOf(
                        Pair(PARAM_PHOTO_DELETE, memberEntity.id)
                    )
                )
            }
            UpdateType.ACTIVE_SESSION -> {
                val paramName = if (memberEntity.isActiveNow()){
                    PARAM_ACTIVE_SESSION_TRUE
                } else {
                    PARAM_ACTIVE_SESSION_FALSE
                }
                analyticsRepository.logEvent(
                    eventName = EVENT_ACTIVE,
                    params = listOf( Pair(paramName, memberEntity.id))
                )
            }
            UpdateType.HAS_COACH -> {
                val paramName = if (memberEntity.hasCoach){
                    PARAM_HAS_COACH_YES
                } else {
                    PARAM_HAS_COACH_NO
                }
                analyticsRepository.logEvent(
                    eventName = EVENT_HAS_COACH,
                    params = listOf( Pair(paramName, memberEntity.id))
                )
            }
        }
        return memberRepository.updateMember(memberEntity)
    }

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_ATTENDANCE_DELETE,
            params = listOf(
                Pair(PARAM_ATTENDANCE_DELETE, attendanceEntity.memberId)
            )
        )
        return memberRepository.deleteAttendance(attendanceEntity)
    }

    suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_MEMBER_DELETE,
            params = listOf(
                Pair(PARAM_MEMBER_DELETE, memberEntity.id
                        + " - " + memberEntity.getFullName())
            )
        )
        return memberRepository.deleteMember(memberEntity)
    }

    suspend fun getAllPaymentPlans(year: Int) =
        memberRepository.getPayments(
            isMembersPayment = false,
            memberId = "",
            year = year
        )

    suspend fun getPaymentPlanForMember(memberId: String, year: Int) =
        memberRepository.getPayments(
            isMembersPayment = true,
            memberId = memberId,
            year = year
        )

    suspend fun addPaymentPlanForMember(paymentEntity: PaymentEntity) =
        memberRepository.addPaymentPlan(paymentEntity)

    suspend fun deletePaymentPlanForMember(paymentEntity: PaymentEntity) =
        memberRepository.deletePaymentPlan(paymentEntity)
}

enum class UpdateType {
    ADMIN_REGISTRATION,
    SELF_REGISTRATION,
    MEMBERSHIP,
    PHOTO_DELETE,
    ACTIVE_SESSION,
    HAS_COACH
}

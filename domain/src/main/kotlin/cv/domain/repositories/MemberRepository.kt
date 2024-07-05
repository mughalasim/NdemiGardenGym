package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.entities.PaymentEntity
import java.util.Date

interface MemberRepository {
    suspend fun getMember(): DomainResult<MemberEntity>

    suspend fun getMemberById(memberId: String): DomainResult<MemberEntity>

    suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>>

    suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean>

    suspend fun getAttendances(
        isMembersAttendances: Boolean,
        memberId: String,
        year: Int,
        month: Int,
    ): DomainResult<Pair<List<AttendanceEntity>, Int>>

    suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ): DomainResult<Unit>

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit>

    suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit>

    suspend fun deletePaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit>

    suspend fun addPaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit>

    suspend fun getPayments(
        isMembersPayment: Boolean,
        memberId: String,
        year: Int,
    ): DomainResult<Triple<List<PaymentEntity>, Boolean, Double>>
}

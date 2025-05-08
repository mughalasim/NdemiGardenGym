package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import java.util.UUID

class PaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val memberRepository: MemberRepository,
) {
    fun getPaymentPlanForMember(
        memberId: String,
        year: Int,
    ) = paymentRepository.getPayments(
        isMembersPayment = true,
        memberId = memberId,
        year = year,
    )

    suspend fun addPaymentPlanForMember(
        memberId: String,
        startDate: Long,
        endDate: Long,
        amount: Double,
        isInTheFuture: Boolean,
    ): DomainResult<Unit> {
        val paymentEntity =
            PaymentEntity(
                paymentId = memberId + UUID.randomUUID().toString(),
                memberId = memberId,
                startDateMillis = startDate,
                endDateMillis = endDate,
                amount = amount,
            )
        val paymentResult = paymentRepository.addPaymentPlan(paymentEntity)

        if (paymentResult is DomainResult.Error || !isInTheFuture) return paymentResult

        return when (val memberResult = memberRepository.getMemberById(memberId)) {
            is DomainResult.Error -> DomainResult.Error(memberResult.error)

            is DomainResult.Success -> {
                memberRepository.updateMember(
                    memberResult.data.copy(renewalFutureDateMillis = endDate, amountDue = amount),
                )
                DomainResult.Success(Unit)
            }
        }
    }

    suspend fun deletePaymentPlanForMember(paymentEntity: PaymentEntity): DomainResult<Unit> {
        val paymentResult = paymentRepository.deletePaymentPlan(paymentEntity)

        if (paymentResult is DomainResult.Error) return paymentResult

        return when (val memberResult = memberRepository.getMemberById(paymentEntity.memberId)) {
            is DomainResult.Error -> DomainResult.Error(memberResult.error)

            is DomainResult.Success -> {
                if (memberResult.data.renewalFutureDateMillis == paymentEntity.endDateMillis) {
                    memberRepository.updateMember(
                        memberResult.data.copy(renewalFutureDateMillis = null, amountDue = 0.0),
                    )
                }
                DomainResult.Success(Unit)
            }
        }
    }
}

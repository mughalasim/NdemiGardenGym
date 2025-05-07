package cv.domain.usecase

import cv.domain.entities.PaymentEntity
import cv.domain.repositories.PaymentRepository

class PaymentUseCase(
    private val paymentRepository: PaymentRepository,
) {
    fun getPaymentPlanForMember(
        memberId: String,
        year: Int,
    ) = paymentRepository.getPayments(
        isMembersPayment = true,
        memberId = memberId,
        year = year,
    )

    suspend fun addPaymentPlanForMember(paymentEntity: PaymentEntity) = paymentRepository.addPaymentPlan(paymentEntity)

    suspend fun deletePaymentPlanForMember(paymentEntity: PaymentEntity) = paymentRepository.deletePaymentPlan(paymentEntity)
}

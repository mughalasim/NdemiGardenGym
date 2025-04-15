package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity

interface PaymentRepository {
    suspend fun getPayments(
        isMembersPayment: Boolean,
        memberId: String,
        year: Int,
    ): DomainResult<Triple<List<PaymentEntity>, Boolean, Double>>

    suspend fun addPaymentPlan(
        paymentEntity: PaymentEntity
    ): DomainResult<Unit>

    suspend fun deletePaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit>
}

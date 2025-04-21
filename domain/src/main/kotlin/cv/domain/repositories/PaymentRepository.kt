package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.entities.PaymentYearEntity

interface PaymentRepository {
    suspend fun getPayments(
        isMembersPayment: Boolean,
        memberId: String,
        year: Int,
    ): DomainResult<PaymentYearEntity>

    suspend fun addPaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit>

    suspend fun deletePaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit>
}

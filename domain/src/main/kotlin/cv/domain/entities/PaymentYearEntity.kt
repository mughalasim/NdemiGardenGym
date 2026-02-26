package cv.domain.entities

data class PaymentYearEntity(
    val payments: List<PaymentEntity> = emptyList(),
    val totalAmount: Double = 0.0,
    val canAddNewPayment: Boolean = false,
)

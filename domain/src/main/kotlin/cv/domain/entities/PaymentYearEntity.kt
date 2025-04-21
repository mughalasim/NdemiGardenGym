package cv.domain.entities

data class PaymentYearEntity(
    val payments: List<PaymentEntity> = emptyList(),
    val totalAmount: Double,
    val canAddNewPayment: Boolean = false,
)

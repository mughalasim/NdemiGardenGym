package cv.domain.entities

data class PaymentEntity(
    val paymentId: String,
    val memberId: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val amount: Double,
)

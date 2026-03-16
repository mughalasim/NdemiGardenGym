package cv.domain.entities

data class PaymentEntity(
    val paymentId: String,
    val memberId: String,
    val startDateMillis: Long = 0,
    val endDateMillis: Long = 0,
    val amount: Double = 0.0,
)

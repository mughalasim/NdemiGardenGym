package cv.domain.entities

data class PaymentEntity(
    val paymentId: String,
    val memberId: String,
    val startDateMillis: Long = 0,
    val startDateDayMonthYear: String = "",
    val endDateMillis: Long = 0,
    val endDateDayMonthYear: String = "",
    val amount: Double = 0.0,
    val paymentPlanDuration: String = "",
)

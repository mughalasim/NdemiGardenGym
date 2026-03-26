package cv.domain.presentationModels

data class PaymentPresentationModel(
    val paymentId: String = "",
    val memberId: String = "",
    val startYear: String = "",
    val endDateMillis: Long = 0,
    val startDateDayMonthYear: String = "",
    val endDateDayMonthYear: String = "",
    val amount: String = "",
    val paymentPlanDuration: String = "",
    val paymentPlanWarningLevel: Int = 0,
)

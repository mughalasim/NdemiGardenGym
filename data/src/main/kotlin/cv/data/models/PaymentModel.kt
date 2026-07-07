package cv.data.models

import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp

@Keep
data class PaymentModel(
    val paymentId: String = "",
    val memberId: String = "",
    val startDate: Timestamp = Timestamp(0, 0),
    val endDate: Timestamp = Timestamp(0, 0),
    val amount: Double = 0.0,
)

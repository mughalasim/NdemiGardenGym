package cv.data.models

import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp
import java.util.Date

@Keep
data class PaymentModel(
    val paymentId: String = "",
    val memberId: String = "",
    val startDate: Timestamp = Timestamp(Date()),
    val endDate: Timestamp = Timestamp(Date()),
    val amount: Double = 0.0,
)

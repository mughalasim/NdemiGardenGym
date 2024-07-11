package cv.data.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import java.util.Date

@Keep
data class MemberModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val registrationDate: Timestamp = Timestamp(Date()),
    val renewalFutureDate: Timestamp? = null,
    val activeNowDate: Timestamp? = null,
    val apartmentNumber: String? = null,
    val profileImageUrl: String? = null,
    val hasCoach: Boolean = false,
    val amountDue: Double = 0.0,
    val phoneNumber: String = "",
    val memberType: String = "",
)

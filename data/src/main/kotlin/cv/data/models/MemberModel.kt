package cv.data.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class MemberModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val registrationDate: Timestamp = Timestamp(0, 0),
    val renewalFutureDate: Timestamp? = null,
    val activeNowDate: Timestamp? = null,
    val apartmentNumber: String = "",
    val profileImageUrl: String? = null,
    val hasCoach: Boolean = false,
    val amountDue: Double = 0.0,
    val phoneNumber: String = "",
    val memberType: String = "",
    val emailVerified: Boolean = false,
    val height: Double = 0.0,
)

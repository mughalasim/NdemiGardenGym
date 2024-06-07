package cv.data.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import cv.domain.entities.MemberEntity
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
) {
    fun toMemberEntity() = MemberEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        registrationDate = registrationDate.toDate(),
        renewalFutureDate = renewalFutureDate?.toDate() ?: run { null },
        activeNowDate = activeNowDate?.toDate() ?: run {null},
    )
}
package cv.data.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime
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
    val profileImageUrl: String? = null
) {
    fun toMemberEntity(): MemberEntity {

        return MemberEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            registrationDate = registrationDate.toDate(),
            renewalFutureDate = isAfterNow(renewalFutureDate),
            activeNowDate = activeNowDate?.toDate() ?: run { null },
            apartmentNumber = apartmentNumber,
            profileImageUrl = profileImageUrl ?: ""
        )
    }

    private fun isAfterNow(timestamp: Timestamp?): Date? {
        if (timestamp == null) return null
        val dateTimeStamp = timestamp.toDate()
        if (DateTime(dateTimeStamp).isBefore(DateTime.now())) return null
        return dateTimeStamp
    }

}

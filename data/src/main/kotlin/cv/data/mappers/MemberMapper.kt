package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime
import java.util.Date

fun MemberEntity.toMemberModel() = MemberModel(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    activeNowDate = activeNowDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
    renewalFutureDate = renewalFutureDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
    registrationDate = Timestamp(Date(registrationDateMillis)),
    apartmentNumber = apartmentNumber,
    profileImageUrl = profileImageUrl,
    hasCoach = hasCoach,
    amountDue = amountDue,
    phoneNumber = phoneNumber,
)


fun MemberModel.toMemberEntity() = MemberEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    registrationDateMillis = registrationDate.toDate().time,
    renewalFutureDateMillis = isAfterNow(renewalFutureDate?.toDate()?.time),
    activeNowDateMillis = activeNowDate?.toDate()?.time ?: run { null },
    apartmentNumber = apartmentNumber,
    profileImageUrl = profileImageUrl.orEmpty(),
    hasCoach = hasCoach,
    amountDue = amountDue,
    phoneNumber = phoneNumber,
)

private fun isAfterNow(millis: Long?): Long? {
    if (millis == null) return null
    if (DateTime(millis).isBefore(DateTime.now())) return null
    return millis
}

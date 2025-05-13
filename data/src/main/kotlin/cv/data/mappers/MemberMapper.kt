package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberType
import org.joda.time.DateTime
import java.util.Date

fun MemberEntity.toMemberModel() =
    MemberModel(
        id = id,
        firstName = firstName.replaceFirstChar(Char::uppercase).trim(),
        lastName = lastName.replaceFirstChar(Char::uppercase).trim(),
        email = email,
        activeNowDate = activeNowDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
        renewalFutureDate = renewalFutureDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
        registrationDate = Timestamp(Date(registrationDateMillis)),
        apartmentNumber = apartmentNumber?.replaceFirstChar(Char::uppercase)?.trim(),
        profileImageUrl = profileImageUrl,
        hasCoach = hasCoach,
        amountDue = amountDue,
        phoneNumber = phoneNumber,
        memberType = memberType.name,
    )

fun MemberModel.toMemberEntity(emailVerified: Boolean = false) =
    MemberEntity(
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
        memberType = memberType.toMemberType(),
        emailVerified = emailVerified,
    )

private fun String.toMemberType(): MemberType =
    when (this) {
        "ADMIN" -> MemberType.ADMIN
        "SUPERVISOR" -> MemberType.SUPERVISOR
        else -> MemberType.MEMBER
    }

private fun isAfterNow(millis: Long?): Long? {
    if (millis == null) return null
    if (DateTime(millis).isBefore(DateTime.now())) return null
    return millis
}

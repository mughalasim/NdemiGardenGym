package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.data.models.WeightModel
import cv.domain.entities.MemberEntity
import cv.domain.entities.WeightEntity
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
        height = if (height.isEmpty()) 0.0 else height.toDouble(),
        trackedWeights = trackedWeights.toWeightModel(),
    )

fun MemberModel.toMemberEntity(emailVerified: Boolean = false): MemberEntity {
    val sortedWeights = trackedWeights.sortedByDescending { it.dateMillis }.take(10)
    return MemberEntity(
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
        height = height.toString(),
        trackedWeights = sortedWeights.toWeightEntity(),
        bmi = sortedWeights.getBMI(height),
    )
}

private fun String.toMemberType(): MemberType =
    when (this) {
        "SUPER_ADMIN" -> MemberType.SUPER_ADMIN
        "ADMIN" -> MemberType.ADMIN
        "SUPERVISOR" -> MemberType.SUPERVISOR
        else -> MemberType.MEMBER
    }

private fun List<WeightModel>.getBMI(height: Double): Double =
    if (this.isEmpty() || height == 0.0) {
        0.0
    } else {
        val heightMeters = height * 0.3048
        val weight = this.first().weight
        weight / (heightMeters * heightMeters)
    }

private fun List<WeightModel>.toWeightEntity(): List<WeightEntity> =
    this.map {
        WeightEntity(
            weight = it.weight.toString(),
            dateMillis = it.dateMillis.toDate().time,
        )
    }

private fun List<WeightEntity>.toWeightModel(): List<WeightModel> =
    this.map {
        WeightModel(
            weight = if (it.weight.isEmpty()) 0.0 else it.weight.toDouble(),
            dateMillis = Timestamp(Date(it.dateMillis)),
        )
    }

private fun isAfterNow(millis: Long?): Long? {
    if (millis == null) return null
    if (DateTime(millis).isBefore(DateTime.now())) return null
    return millis
}

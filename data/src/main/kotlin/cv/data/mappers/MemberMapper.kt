package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity

fun MemberEntity.toMemberModel() = MemberModel(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    activeNowDate = activeNowDate?.let { Timestamp(it) }?: run { null },
    renewalFutureDate = renewalFutureDate?.let { Timestamp(it) }?: run { null },
    registrationDate = Timestamp(registrationDate),
    apartmentNumber = apartmentNumber
)
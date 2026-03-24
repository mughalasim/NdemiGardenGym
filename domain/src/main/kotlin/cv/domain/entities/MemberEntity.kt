package cv.domain.entities

import cv.domain.enums.MemberType

data class MemberEntity(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val registrationDateMillis: Long = 0L,
    val renewalFutureDateMillis: Long? = null,
    val activeNowDateMillis: Long? = null,
    val apartmentNumber: String = "",
    val profileImageUrl: String = "",
    val hasCoach: Boolean = false,
    val amountDue: Double = 0.0,
    val phoneNumber: String = "",
    val memberType: MemberType = MemberType.MEMBER,
    val emailVerified: Boolean = false,
    val height: Double = 0.0,
)

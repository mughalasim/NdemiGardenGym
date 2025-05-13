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
    val apartmentNumber: String? = null,
    val profileImageUrl: String = "",
    val hasCoach: Boolean = false,
    val amountDue: Double = 0.0,
    val phoneNumber: String = "",
    val memberType: MemberType = MemberType.MEMBER,
    val emailVerified: Boolean = false,
) {
    fun getFullName(): String = "$firstName $lastName"

    fun hasPaidMembership(): Boolean = renewalFutureDateMillis != null

    fun isActiveNow(): Boolean = activeNowDateMillis != null

    fun isAdmin() = memberType == MemberType.ADMIN

    fun getResidentialStatus(): String =
        if (apartmentNumber.isNullOrEmpty()) {
            "Guest"
        } else {
            "Apartment $apartmentNumber"
        }

    fun getCoachStatus(): String = if (hasCoach) "Yes" else "No"

    fun isNotEqualTo(memberEntity: MemberEntity?): Boolean {
        if (memberEntity == null) return true
        return memberEntity.firstName != firstName ||
            memberEntity.lastName != lastName ||
            memberEntity.apartmentNumber != apartmentNumber ||
            memberEntity.phoneNumber != phoneNumber ||
            memberEntity.hasCoach != hasCoach
    }
}

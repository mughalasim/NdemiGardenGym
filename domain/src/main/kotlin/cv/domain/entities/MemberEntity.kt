package cv.domain.entities

import cv.domain.enums.MemberType
import java.text.DecimalFormat

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
    val trackedWeights: List<WeightEntity> = listOf(),
    val height: String = "",
    val bmi: Double = 0.0,
) {
    fun getFullName(): String = "$firstName $lastName"

    fun hasPaidMembership(): Boolean = renewalFutureDateMillis != null

    fun isActiveNow(): Boolean = activeNowDateMillis != null

    fun getResidentialStatus(): String =
        if (apartmentNumber.isEmpty()) {
            "Guest"
        } else {
            "Apartment $apartmentNumber"
        }

    // TODO - figure out metrics measurement
    fun getDisplayHeight() = if (height.isEmpty()) "-" else "$height ft"

    // TODO - figure out metrics measurement
    fun getDisplayWeight() = if (trackedWeights.isEmpty()) "-" else trackedWeights.first().weight + " Kgs"

    fun getDisplayBMI(): String = DecimalFormat("##.#").format(bmi)

    fun getCoachStatus(): String = if (hasCoach) "Yes" else "No"

    fun isNotEqualTo(memberEntity: MemberEntity?): Boolean {
        if (memberEntity == null) return true
        return memberEntity.firstName != firstName ||
            memberEntity.lastName != lastName ||
            memberEntity.apartmentNumber != apartmentNumber ||
            memberEntity.phoneNumber != phoneNumber ||
            memberEntity.hasCoach != hasCoach ||
            memberEntity.height != height
    }
}

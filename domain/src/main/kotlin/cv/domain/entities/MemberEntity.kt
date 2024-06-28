package cv.domain.entities

data class MemberEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val registrationDateMillis: Long,
    val renewalFutureDateMillis: Long? = null,
    val activeNowDateMillis: Long? = null,
    val apartmentNumber: String? = null,
    val profileImageUrl: String,
    val hasCoach: Boolean = false,
){
    fun getFullName(): String = "$firstName $lastName"

    fun hasPaidMembership(): Boolean = renewalFutureDateMillis != null

    fun isActiveNow(): Boolean = activeNowDateMillis != null

    fun getResidentialStatus(): String =
        if (apartmentNumber.isNullOrEmpty()) {
            "Guest member"
        } else {
            "Apartment number: $apartmentNumber"
        }

    fun getCoachStatus(): String = if (hasCoach) "Yes" else "No"
}

package cv.domain.entities

import java.util.Date

data class MemberEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val registrationDate: Date,
    val renewalFutureDate: Date? = null,
    val activeNowDate: Date? = null,
    val apartmentNumber: String? = null,
    val profileImageUrl: String,
    val hasCoach: Boolean = false,
){
    fun getFullName(): String = "$firstName $lastName"

    fun hasPaidMembership(): Boolean = renewalFutureDate != null

    fun isActiveNow(): Boolean = activeNowDate != null

    fun getResidentialStatus(): String =
        if (apartmentNumber.isNullOrEmpty()) {
            "Guest member"
        } else {
            "Apartment number: $apartmentNumber"
        }

    fun getCoachStatus(): String = if (hasCoach) "Yes" else "No"

}

fun getMockMemberEntity() = MemberEntity(
    id= "1234567890",
    firstName = "Asim",
    lastName = "Mughal",
    email = "asim@test.com",
    registrationDate = Date(),
    renewalFutureDate = Date(),
    activeNowDate = Date(),
    apartmentNumber = "",
    profileImageUrl = "",
    hasCoach = true
)

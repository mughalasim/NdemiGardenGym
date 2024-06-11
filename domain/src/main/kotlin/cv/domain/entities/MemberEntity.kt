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
    val apartmentNumber: String? = null
){
    fun getFullName(): String = "$firstName $lastName"

    fun hasPaidMembership(): Boolean = renewalFutureDate != null

    fun isActiveNow(): Boolean = activeNowDate != null

    fun getResidentialStatus(): String =
        if (!apartmentNumber.isNullOrEmpty()) {
            "Apartment number: $apartmentNumber"
        } else {
            "Guest member"
        }
}

fun getMockMemberEntity() = MemberEntity(
    id= "1234567890",
    firstName = "Asim",
    lastName = "Mughal",
    email = "asim@test.com",
    registrationDate = Date(),
    renewalFutureDate = Date(),
    activeNowDate = Date(),
    apartmentNumber = ""
)

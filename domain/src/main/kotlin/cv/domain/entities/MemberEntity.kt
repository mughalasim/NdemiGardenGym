package cv.domain.entities

data class MemberEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val registrationDate: String,
    val renewalFutureDate: String? = null,
    val activeNowDate: String? = null,
)

fun getMockMemberEntity() = MemberEntity(
    id= "1234567890",
    firstName = "Asim",
    lastName = "Mughal",
    email = "asim@test.com",
    registrationDate = "12/12/2023"
)

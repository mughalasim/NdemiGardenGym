package cv.domain.presentationModels

data class MemberPresentationModel(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val hasCoach: Boolean = false,
    val isActive: Boolean = false,
    val lastActive: String = "",
    val activeNowDateMillis: Long? = null,
    val amountDue: String = "",
    val phoneNumber: String = "",
    val memberType: String = "",
    val emailVerified: Boolean = false,
    val hasPaidMembership: Boolean = false,
    val residentialStatus: String = "",
    val membershipRenewalDate: String = "",
    val height: String = "",
)

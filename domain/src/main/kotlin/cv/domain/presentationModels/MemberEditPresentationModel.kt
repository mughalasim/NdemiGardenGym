package cv.domain.presentationModels

data class MemberEditPresentationModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val hasCoach: Boolean = false,
    val phoneNumber: String = "",
    val memberType: String = "",
    val apartmentNumber: String = "",
    val registrationDate: String = "",
    val height: String = "",
    val heightUnit: String = "",
)

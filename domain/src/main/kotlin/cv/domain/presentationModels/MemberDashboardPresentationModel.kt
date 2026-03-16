package cv.domain.presentationModels

data class MemberDashboardPresentationModel(
    val id: String = "",
    val fullName: String = "",
    val profileImageUrl: String = "",
    val hasCoach: Boolean = false,
    val isActive: Boolean = false,
    val lastActive: String = "",
    val activeNowDateMillis: Long? = null,
    val amountDue: String = "",
    val hasPaidMembership: Boolean = false,
    val registrationDate: String = "",
    val height: String = "",
    val heightUnit: String = "",
    val weight: String = "",
    val weightUnit: String = "",
    val bmiValue: Double = 0.0,
    val workouts: String = "",
)

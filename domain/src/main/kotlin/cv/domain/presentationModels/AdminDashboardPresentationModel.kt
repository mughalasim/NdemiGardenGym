package cv.domain.presentationModels

data class AdminDashboardPresentationModel(
    val selectedYear: Int = 2025,
    val selectedMonth: String = "",
    val totalRegisteredUsers: Int = 0,
    val totalExpiredUsers: Int = 0,
    val totalRevenueYear: String = "",
    val totalRevenueMonth: String = "",
    val topTenPayingMembers: List<TopTenMemberPresentationModel> = listOf(),
    val topTenActiveMembers: List<TopTenMemberPresentationModel> = listOf(),
)

data class TopTenMemberPresentationModel(
    val id: String = "",
    val fullName: String = "",
    val visits: Int = 0,
    val amountFormatted: String = "",
    val amountValue: Double = 0.0,
)

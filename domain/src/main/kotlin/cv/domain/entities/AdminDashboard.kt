package cv.domain.entities

data class AdminDashboard(
    val memberEntity: MemberEntity = MemberEntity(),
    val totalRegisteredUsers: Int = 0,
    val totalExpiredUsers: Int = 0,
    val totalRevenueYear: Double = 0.0,
    val totalRevenueMonth: Double = 0.0,
    val topTenPayingMembers: List<Pair<MemberEntity, Double>> = listOf(),
    val topTenActiveMembers: List<Pair<MemberEntity, Int>> = listOf(),
)

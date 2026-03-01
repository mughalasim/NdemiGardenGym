package cv.domain.entities

data class AttendanceMonthEntity(
    val monthNumber: Int = 1,
    val monthName: String = "",
    val totalMinutes: Int = 0,
    val activeDuration: String = "",
    val attendances: List<AttendanceEntity> = emptyList(),
)

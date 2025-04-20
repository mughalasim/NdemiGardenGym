package cv.domain.entities

data class AttendanceMonthEntity(
    val monthName: String,
    val totalMinutes: Int = 0,
    val attendances: List<AttendanceEntity> = emptyList()
)

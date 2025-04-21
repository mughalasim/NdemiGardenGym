package cv.domain.entities

data class AttendanceMonthEntity(
    val monthNumber: Int = 1,
    val totalMinutes: Int = 0,
    val attendances: List<AttendanceEntity> = emptyList()
)

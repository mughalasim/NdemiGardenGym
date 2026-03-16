package cv.domain.entities

data class AttendanceEntity(
    val attendanceId: String = "",
    val memberId: String,
    val startDateMillis: Long = 0,
    val endDateMillis: Long = 0,
)

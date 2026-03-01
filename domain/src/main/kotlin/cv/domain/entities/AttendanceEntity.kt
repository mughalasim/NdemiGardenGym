package cv.domain.entities

data class AttendanceEntity(
    val memberId: String,
    val startDateMillis: Long = 0,
    val endDateMillis: Long = 0,
    val startDateDay: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val activeStatusDuration: String = "",
)

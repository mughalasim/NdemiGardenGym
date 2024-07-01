package cv.domain.entities

data class AttendanceEntity (
    val memberId: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
)

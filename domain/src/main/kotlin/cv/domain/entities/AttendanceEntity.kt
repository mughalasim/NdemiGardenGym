package cv.domain.entities

import java.util.Date

data class AttendanceEntity (
    val memberId: String,
    val startDate: Date,
    val endDate: Date,
)

fun getMockAttendanceEntity(
    startDate: Date = Date(),
    endDate: Date = Date()
) = AttendanceEntity(
    memberId = "1234567890",
    startDate = startDate,
    endDate = endDate,
)

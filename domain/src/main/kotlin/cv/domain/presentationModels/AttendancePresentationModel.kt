package cv.domain.presentationModels

data class AttendancePresentationModel(
    val attendanceId: String = "",
    val startYear: String = "",
    val startMonth: String = "",
    val memberId: String = "",
    val startDateDay: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val activeStatusDuration: String = "",
)

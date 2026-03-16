package cv.domain.presentationModels

data class AttendanceMonthPresentationModel(
    val monthNumber: Int = 1,
    val monthName: String = "",
    val totalMinutes: Int = 0,
    val activeDuration: String = "",
    val attendances: List<AttendancePresentationModel> = emptyList(),
)

package cv.domain.presentationModels

data class WeightPresentationModel(
    val id: String = "",
    val formattedWeight: String = "",
    val formattedDate: String = "",
    val weightValue: String = "",
    val weightUnit: String = "",
    val dateMillis: Long = 0L,
)

package cv.domain.presentationModels

data class WeightEditPresentationModel(
    val id: String = "",
    val formattedDate: String = "",
    val formattedWeight: String = "",
    val weightUnit: String = "",
    val dateMillis: Long = 0L,
)

package cv.data.models

import com.google.firebase.Timestamp

data class WeightModel(
    val id: String = "",
    val year: Int = 0, // eg 2025
    val dateMillis: Timestamp = Timestamp(0, 0),
    val weight: Double = 0.0, // in Kgs
)

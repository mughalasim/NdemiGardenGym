package cv.data.models

import com.google.firebase.Timestamp
import java.util.Date

data class WeightModel(
    val dateMillis: Timestamp = Timestamp(Date()),
    val weight: Double = 0.0,
)

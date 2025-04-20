package cv.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class VersionModel(
    val version: Int = 0,
    val url: String = "",
)

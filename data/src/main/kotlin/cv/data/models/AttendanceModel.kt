package cv.data.models

import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp

@Keep
data class AttendanceModel(
    val memberId: String = "",
    val startDate: Timestamp = Timestamp(0, 0),
    val endDate: Timestamp = Timestamp(0, 0),
) {
    fun getAttendanceId(): String = "$memberId-$startDate"

    fun setAttendanceId(id: String) = Unit
}

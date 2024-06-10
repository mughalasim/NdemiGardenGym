package cv.data.models

import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp
import cv.domain.entities.AttendanceEntity
import java.util.Date

@Keep
data class AttendanceModel(
    val memberId: String = "",
    val startDate: Timestamp = Timestamp(Date()),
    val endDate: Timestamp = Timestamp(Date()),
) {

    fun getAttendanceId(): String = "$memberId-$startDate"

    fun toAttendanceEntity() = AttendanceEntity(
        memberId = memberId,
        startDate = startDate.toDate(),
        endDate = endDate.toDate()
    )
}

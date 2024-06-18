package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity

fun AttendanceEntity.toAttendanceModel() = AttendanceModel(
    memberId = memberId,
    startDate = Timestamp(startDate),
    endDate = Timestamp(endDate)
)

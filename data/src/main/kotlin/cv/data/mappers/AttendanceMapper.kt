package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity
import java.util.Date

fun AttendanceEntity.toAttendanceModel() =
    AttendanceModel(
        memberId = memberId,
        startDate = Timestamp(Date(startDateMillis)),
        endDate = Timestamp(Date(endDateMillis)),
    )

fun AttendanceModel.toAttendanceEntity() =
    AttendanceEntity(
        memberId = memberId,
        startDateMillis = startDate.toDate().time,
        endDateMillis = endDate.toDate().time,
    )

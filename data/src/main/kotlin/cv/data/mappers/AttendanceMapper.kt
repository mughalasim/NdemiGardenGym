package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity
import cv.domain.repositories.DateProviderRepository
import java.util.Date

fun AttendanceEntity.toAttendanceModel() =
    AttendanceModel(
        memberId = memberId,
        startDate = Timestamp(Date(startDateMillis)),
        endDate = Timestamp(Date(endDateMillis)),
    )

fun AttendanceModel.toAttendanceEntity(dateProviderRepository: DateProviderRepository): AttendanceEntity {
    val startDateTime = startDate.toDate().time
    val endDateTime = endDate.toDate().time
    return AttendanceEntity(
        memberId = memberId,
        startDateMillis = startDateTime,
        endDateMillis = endDateTime,
        startDateDay = dateProviderRepository.formatDateDay(startDateTime),
        startTime = dateProviderRepository.formatTime(startDateTime),
        endTime = dateProviderRepository.formatTime(endDateTime),
        activeStatusDuration = dateProviderRepository.activeStatusDuration(startDateTime, endDateTime),
    )
}

package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity
import cv.domain.enums.DateFormatType
import cv.domain.repositories.DateProviderRepository
import java.util.Date

class AttendanceMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : AttendanceMapper {
    override fun getModel(entity: AttendanceEntity): AttendanceModel = entity.toModel()

    override fun getEntity(model: AttendanceModel): AttendanceEntity = model.toEntity()

    private fun AttendanceEntity.toModel() =
        AttendanceModel(
            memberId = memberId,
            startDate = Timestamp(Date(startDateMillis)),
            endDate = Timestamp(Date(endDateMillis)),
        )

    private fun AttendanceModel.toEntity(): AttendanceEntity {
        val startDateTime = startDate.toDate().time
        val endDateTime = endDate.toDate().time
        return AttendanceEntity(
            memberId = memberId,
            startDateMillis = startDateTime,
            endDateMillis = endDateTime,
            startDateDay = dateProviderRepository.format(startDateTime, DateFormatType.DATE_DAY),
            startTime = dateProviderRepository.format(startDateTime, DateFormatType.TIME),
            endTime = dateProviderRepository.format(endDateTime, DateFormatType.TIME),
            activeStatusDuration = dateProviderRepository.activeStatusDuration(startDateTime, endDateTime),
        )
    }
}

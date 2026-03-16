package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity
import cv.domain.repositories.DateProviderRepository

class AttendanceMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : AttendanceMapper {
    override fun getModel(entity: AttendanceEntity): AttendanceModel = entity.toModel()

    override fun getEntity(model: AttendanceModel): AttendanceEntity = model.toEntity()

    private fun AttendanceEntity.toModel() =
        AttendanceModel(
            memberId = memberId,
            startDate = Timestamp(dateProviderRepository.getDate(startDateMillis)),
            endDate = Timestamp(dateProviderRepository.getDate(endDateMillis)),
        )

    private fun AttendanceModel.toEntity(): AttendanceEntity {
        val startDateTime = startDate.toDate().time
        val endDateTime = endDate.toDate().time
        return AttendanceEntity(
            attendanceId = getAttendanceId(),
            memberId = memberId,
            startDateMillis = startDateTime,
            endDateMillis = endDateTime,
        )
    }
}

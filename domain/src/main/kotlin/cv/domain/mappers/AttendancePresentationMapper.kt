package cv.domain.mappers

import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.AttendanceMonthPresentationModel
import cv.domain.presentationModels.AttendancePresentationModel
import cv.domain.repositories.DateProviderRepository

interface AttendancePresentationMapper {
    fun getModel(entity: AttendanceEntity): AttendancePresentationModel

    fun getModel(entity: AttendanceMonthEntity): AttendanceMonthPresentationModel
}

class AttendancePresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : AttendancePresentationMapper {
    override fun getModel(entity: AttendanceEntity): AttendancePresentationModel =
        AttendancePresentationModel(
            attendanceId = entity.attendanceId,
            startYear = dateProviderRepository.getYear(entity.startDateMillis).toString(),
            startMonth = dateProviderRepository.getMonth(entity.startDateMillis).toString(),
            memberId = entity.memberId,
            startDateDay = dateProviderRepository.format(entity.startDateMillis, DateFormatType.DATE_DAY),
            startTime = dateProviderRepository.format(entity.startDateMillis, DateFormatType.TIME),
            endTime = dateProviderRepository.format(entity.endDateMillis, DateFormatType.TIME),
            activeStatusDuration = dateProviderRepository.activeStatusDuration(entity.startDateMillis, entity.endDateMillis),
        )

    override fun getModel(entity: AttendanceMonthEntity): AttendanceMonthPresentationModel =
        AttendanceMonthPresentationModel(
            monthNumber = entity.monthNumber,
            monthName = dateProviderRepository.getMonthName(entity.monthNumber),
            totalMinutes = entity.totalMinutes,
            activeDuration = dateProviderRepository.activeStatusDuration(entity.totalMinutes),
            attendances = entity.attendances.map { getModel(it) },
        )
}

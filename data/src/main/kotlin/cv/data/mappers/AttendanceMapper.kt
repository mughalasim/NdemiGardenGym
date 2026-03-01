package cv.data.mappers

import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity

interface AttendanceMapper {
    fun getModel(entity: AttendanceEntity): AttendanceModel

    fun getEntity(model: AttendanceModel): AttendanceEntity
}

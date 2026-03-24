package cv.data.mappers

import cv.data.models.WeightModel
import cv.domain.entities.WeightEntity

interface WeightMapper {
    fun getModel(entity: WeightEntity): WeightModel

    fun getEntity(model: WeightModel): WeightEntity
}

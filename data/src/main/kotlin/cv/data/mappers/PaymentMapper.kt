package cv.data.mappers

import cv.data.models.PaymentModel
import cv.domain.entities.PaymentEntity

interface PaymentMapper {
    fun getModel(entity: PaymentEntity): PaymentModel

    fun getEntity(model: PaymentModel): PaymentEntity
}

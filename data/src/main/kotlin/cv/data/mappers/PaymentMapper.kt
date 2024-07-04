package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.PaymentModel
import cv.domain.entities.PaymentEntity
import java.util.Date

fun PaymentEntity.toPaymentModel() = PaymentModel(
    paymentId = paymentId,
    memberId = memberId,
    startDate = Timestamp(Date(startDateMillis)),
    endDate = Timestamp(Date(endDateMillis)),
    amount = amount,
)

fun PaymentModel.toPaymentEntity() = PaymentEntity(
    paymentId = paymentId,
    memberId = memberId,
    startDateMillis = startDate.toDate().time,
    endDateMillis = endDate.toDate().time,
    amount = amount,
)

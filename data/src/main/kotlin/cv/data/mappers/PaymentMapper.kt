package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.PaymentModel
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.DateProviderRepository
import java.util.Date

fun PaymentEntity.toPaymentModel() =
    PaymentModel(
        paymentId = paymentId,
        memberId = memberId,
        startDate = Timestamp(Date(startDateMillis)),
        endDate = Timestamp(Date(endDateMillis)),
        amount = amount,
    )

fun PaymentModel.toPaymentEntity(dateProviderRepository: DateProviderRepository): PaymentEntity {
    val startTime = startDate.toDate().time
    val endTime = endDate.toDate().time
    return PaymentEntity(
        paymentId = paymentId,
        memberId = memberId,
        startDateMillis = startTime,
        startDateDayMonthYear = dateProviderRepository.formatDayMonthYear(startTime),
        endDateMillis = endTime,
        endDateDayMonthYear = dateProviderRepository.formatDayMonthYear(endTime),
        amount = amount,
        paymentPlanDuration = dateProviderRepository.toPaymentPlanDuration(endTime),
    )
}

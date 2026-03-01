package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.PaymentModel
import cv.domain.entities.PaymentEntity
import cv.domain.enums.DateFormatType
import cv.domain.repositories.DateProviderRepository
import java.util.Date

class PaymentMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : PaymentMapper {
    override fun getModel(entity: PaymentEntity): PaymentModel = entity.toModel()

    override fun getEntity(model: PaymentModel): PaymentEntity = model.toEntity()

    private fun PaymentEntity.toModel() =
        PaymentModel(
            paymentId = paymentId,
            memberId = memberId,
            startDate = Timestamp(Date(startDateMillis)),
            endDate = Timestamp(Date(endDateMillis)),
            amount = amount,
        )

    private fun PaymentModel.toEntity(): PaymentEntity {
        val startTime = startDate.toDate().time
        val endTime = endDate.toDate().time
        return PaymentEntity(
            paymentId = paymentId,
            memberId = memberId,
            startDateMillis = startTime,
            startDateDayMonthYear = dateProviderRepository.format(startTime, DateFormatType.DAY_MONTH_YEAR),
            endDateMillis = endTime,
            endDateDayMonthYear = dateProviderRepository.format(endTime, DateFormatType.DAY_MONTH_YEAR),
            amount = amount,
            paymentPlanDuration = dateProviderRepository.toPaymentPlanDuration(endTime),
        )
    }
}

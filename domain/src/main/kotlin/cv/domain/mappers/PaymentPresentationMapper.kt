package cv.domain.mappers

import cv.domain.entities.PaymentEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.PaymentPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase

interface PaymentPresentationMapper {
    fun getModel(entity: PaymentEntity): PaymentPresentationModel
}

class PaymentPresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) : PaymentPresentationMapper {
    override fun getModel(entity: PaymentEntity): PaymentPresentationModel {
        val planDuration = dateProviderRepository.toPaymentPlanDuration(entity.endDateMillis)
        return PaymentPresentationModel(
            paymentId = entity.paymentId,
            memberId = entity.memberId,
            startYear = dateProviderRepository.getYear(entity.startDateMillis).toString(),
            endDateMillis = entity.endDateMillis,
            startDateDayMonthYear = dateProviderRepository.format(entity.startDateMillis, DateFormatType.DAY_MONTH_YEAR),
            endDateDayMonthYear = dateProviderRepository.format(entity.endDateMillis, DateFormatType.DAY_MONTH_YEAR),
            amount = numberFormatUseCase.getCurrencyFormatted(entity.amount),
            paymentPlanDuration = if (planDuration.isEmpty()) "" else "Expires in $planDuration",
        )
    }
}

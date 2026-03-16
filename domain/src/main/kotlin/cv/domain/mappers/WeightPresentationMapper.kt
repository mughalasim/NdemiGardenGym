package cv.domain.mappers

import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase

interface WeightPresentationMapper {
    fun getModel(entity: WeightEntity): WeightPresentationModel
}

class WeightPresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) : WeightPresentationMapper {
    override fun getModel(entity: WeightEntity): WeightPresentationModel =
        WeightPresentationModel(
            dateMillis = entity.dateMillis,
            weight = "${numberFormatUseCase.getWeight(listOf(entity))} ${numberFormatUseCase.getWeightUnit()}",
            dateDayMonthYear = dateProviderRepository.format(entity.dateMillis, DateFormatType.DAY_MONTH_YEAR),
        )
}

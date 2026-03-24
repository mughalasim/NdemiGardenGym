package cv.domain.mappers

import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase

interface WeightPresentationMapper {
    fun getModel(entity: WeightEntity): WeightPresentationModel

    fun getEntity(model: WeightPresentationModel): WeightEntity
}

class WeightPresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) : WeightPresentationMapper {
    override fun getModel(entity: WeightEntity): WeightPresentationModel =
        WeightPresentationModel(
            id = entity.id,
            dateMillis = entity.dateMillis,
            formattedWeight = "${numberFormatUseCase.getWeight(listOf(entity))} ${numberFormatUseCase.getWeightUnit()}",
            formattedDate = dateProviderRepository.format(entity.dateMillis, DateFormatType.DAY_MONTH_YEAR),
            weightValue = entity.weight.toString(),
            weightUnit = numberFormatUseCase.getWeightUnit(),
        )

    override fun getEntity(model: WeightPresentationModel) =
        WeightEntity(
            id = model.id,
            dateMillis = model.dateMillis,
            weight = model.weightValue.toDouble(),
        )
}

package cv.domain.mappers

import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase

interface WeightPresentationMapper {
    fun getModel(entity: WeightEntity): WeightPresentationModel

    fun getEditModel(entity: WeightEntity): WeightEditPresentationModel

    fun getEntity(model: WeightEditPresentationModel): WeightEntity
}

class WeightPresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) : WeightPresentationMapper {
    override fun getModel(entity: WeightEntity): WeightPresentationModel =
        WeightPresentationModel(
            id = entity.id,
            dateMillis = entity.dateMillis,
            formattedWeight = "${numberFormatUseCase.getWeight(entity.weight)} ${numberFormatUseCase.getWeightUnit()}",
            formattedDate = dateProviderRepository.format(entity.dateMillis, DateFormatType.DAY_MONTH_YEAR),
            weightValue = entity.weight.toString(),
            weightUnit = numberFormatUseCase.getWeightUnit(),
        )

    override fun getEditModel(entity: WeightEntity) =
        WeightEditPresentationModel(
            id = entity.id,
            formattedDate = dateProviderRepository.format(entity.dateMillis, DateFormatType.DAY_MONTH_YEAR),
            formattedWeight = numberFormatUseCase.getWeight(entity.weight).toString(),
            weightUnit = numberFormatUseCase.getWeightUnit(),
            dateMillis = entity.dateMillis,
        )

    override fun getEntity(model: WeightEditPresentationModel) =
        WeightEntity(
            id = model.id,
            dateMillis = model.dateMillis,
            weight = numberFormatUseCase.setWeight(model.formattedWeight.toDouble()),
        )
}

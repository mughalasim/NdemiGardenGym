package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.repositories.WeightRepository
import java.util.UUID

class WeightUseCase(
    private val weightRepository: WeightRepository,
    private val weightPresentationMapper: WeightPresentationMapper,
) {
    suspend fun setWeight(model: WeightEditPresentationModel): DomainResult<Unit> {
        val entity =
            weightPresentationMapper.getEntity(
                if (model.id.isEmpty()) {
                    model.copy(id = UUID.randomUUID().toString())
                } else {
                    model
                },
            )
        return weightRepository.setWeight(entity)
    }

    suspend fun deleteWeight(weightId: String) = weightRepository.deleteWeight(weightId)

    fun getWeightForYear(year: Int) = weightRepository.getWeightForYear(year = year)
}

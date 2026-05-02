package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.dispatchers.ScopeProvider
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.repositories.WeightRepository
import kotlinx.coroutines.withContext
import java.util.UUID

class WeightUseCase(
    private val scope: ScopeProvider,
    private val weightRepository: WeightRepository,
    private val weightPresentationMapper: WeightPresentationMapper,
) {
    suspend fun setWeight(model: WeightEditPresentationModel): DomainResult<Unit> =
        withContext(scope.ioDispatcher()) {
            val entity =
                weightPresentationMapper.getEntity(
                    if (model.id.isEmpty()) {
                        model.copy(id = UUID.randomUUID().toString())
                    } else {
                        model
                    },
                )
            return@withContext weightRepository.setWeight(entity)
        }

    suspend fun deleteWeight(weightId: String) = withContext(scope.ioDispatcher()) { weightRepository.deleteWeight(weightId) }

    fun getWeightForYear(year: Int) = weightRepository.getWeightForYear(year = year)
}

package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.WeightEntity
import kotlinx.coroutines.flow.Flow

interface WeightRepository {
    fun getWeightForYear(year: Int): Flow<DomainResult<List<WeightEntity>>>

    suspend fun setWeight(weightEntity: WeightEntity): DomainResult<Unit>

    suspend fun deleteWeight(weightId: String): DomainResult<Unit>
}

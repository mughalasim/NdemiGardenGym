package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.WeightModel
import cv.domain.entities.WeightEntity
import cv.domain.repositories.DateProviderRepository

class WeightMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : WeightMapper {
    override fun getModel(entity: WeightEntity) = entity.toModel()

    override fun getEntity(model: WeightModel) = model.toEntity()

    private fun WeightEntity.toModel() =
        WeightModel(
            id = id,
            year = dateProviderRepository.getYear(dateMillis),
            dateMillis = Timestamp(dateProviderRepository.getDate(dateMillis)),
            weight = weight,
        )

    private fun WeightModel.toEntity() =
        WeightEntity(
            id = id,
            dateMillis = dateMillis.toDate().time,
            weight = weight,
        )
}

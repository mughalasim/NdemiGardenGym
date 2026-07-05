package cv.domain.mappers

import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.enums.unit.WeightUnit
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeightPresentationMapperTest {
    private lateinit var mapper: WeightPresentationMapper
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()

    @Before
    fun setUp() {
        mapper = WeightPresentationMapperImp(dateProviderRepository, numberFormatUseCase)
    }

    @Test
    fun `getModel should return correct WeightPresentationModel`() {
        // Given
        val entity = WeightEntity(id = "1", dateMillis = 1000L, weight = 80.0)

        every { numberFormatUseCase.getWeight(80.0) } returns 80.0
        every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS
        every { dateProviderRepository.format(1000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-01-2023"

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals("1", result.id)
        assertEquals("80.0 Kg", result.formattedWeight)
        assertEquals("01-01-2023", result.formattedDate)
        assertEquals("80.0", result.weightValue)
    }

    @Test
    fun `getEditModel should return correct WeightEditPresentationModel`() {
        // Given
        val entity = WeightEntity(id = "1", dateMillis = 1000L, weight = 80.0)

        every { dateProviderRepository.format(1000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-01-2023"
        every { numberFormatUseCase.getWeight(80.0) } returns 80.0
        every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS

        // When
        val result = mapper.getEditModel(entity)

        // Then
        assertEquals("1", result.id)
        assertEquals("80.0", result.formattedWeight)
        assertEquals("Kilograms", result.weightUnit)
    }

    @Test
    fun `getEntity should return correct WeightEntity`() {
        // Given
        val model =
            WeightEditPresentationModel(
                id = "1",
                formattedDate = "01-01-2023",
                formattedWeight = "80.0",
                weightUnit = "Kilograms",
                dateMillis = 1000L,
            )

        every { numberFormatUseCase.setWeight(80.0) } returns 80.0

        // When
        val result = mapper.getEntity(model)

        // Then
        assertEquals("1", result.id)
        assertEquals(80.0, result.weight, 0.0)
        assertEquals(1000L, result.dateMillis)
    }
}

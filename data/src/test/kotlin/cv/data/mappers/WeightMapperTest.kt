package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.WeightModel
import cv.domain.entities.WeightEntity
import cv.domain.repositories.DateProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class WeightMapperTest {
    private lateinit var mapper: WeightMapper
    private val dateProviderRepository: DateProviderRepository = mockk()

    @Before
    fun setUp() {
        mapper = WeightMapperImp(dateProviderRepository)
    }

    @Test
    fun `getModel should return correct WeightModel`() {
        // Given
        val dateMillis = 1672531200000L // 2023-01-01
        val entity =
            WeightEntity(
                id = "1",
                dateMillis = dateMillis,
                weight = 75.5,
            )
        val expectedDate = Date(dateMillis)
        val expectedYear = 2023

        every { dateProviderRepository.getYear(dateMillis) } returns expectedYear
        every { dateProviderRepository.getDate(dateMillis) } returns expectedDate

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals(entity.id, result.id)
        assertEquals(expectedYear, result.year)
        assertEquals(Timestamp(expectedDate), result.dateMillis)
        assertEquals(entity.weight, result.weight, 0.0)
    }

    @Test
    fun `getEntity should return correct WeightEntity`() {
        // Given
        val dateMillis = 1672531200000L
        val timestamp = Timestamp(Date(dateMillis))
        val model =
            WeightModel(
                id = "1",
                year = 2023,
                dateMillis = timestamp,
                weight = 75.5,
            )

        // When
        val result = mapper.getEntity(model)

        // Then
        assertEquals(model.id, result.id)
        assertEquals(dateMillis, result.dateMillis)
        assertEquals(model.weight, result.weight, 0.0)
    }
}

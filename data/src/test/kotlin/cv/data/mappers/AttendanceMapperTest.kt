package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.AttendanceModel
import cv.domain.entities.AttendanceEntity
import cv.domain.repositories.DateProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class AttendanceMapperTest {
    private lateinit var mapper: AttendanceMapper
    private val dateProviderRepository: DateProviderRepository = mockk()

    @Before
    fun setUp() {
        mapper = AttendanceMapperImp(dateProviderRepository)
    }

    @Test
    fun `getModel should return correct AttendanceModel`() {
        // Given
        val startMillis = 1672531200000L
        val endMillis = 1672534800000L
        val entity =
            AttendanceEntity(
                memberId = "member1",
                startDateMillis = startMillis,
                endDateMillis = endMillis,
            )
        val startDate = Date(startMillis)
        val endDate = Date(endMillis)

        every { dateProviderRepository.getDate(startMillis) } returns startDate
        every { dateProviderRepository.getDate(endMillis) } returns endDate

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals(entity.memberId, result.memberId)
        assertEquals(Timestamp(startDate), result.startDate)
        assertEquals(Timestamp(endDate), result.endDate)
    }

    @Test
    fun `getEntity should return correct AttendanceEntity`() {
        // Given
        val startMillis = 1672531200000L
        val endMillis = 1672534800000L
        val model =
            AttendanceModel(
                memberId = "member1",
                startDate = Timestamp(Date(startMillis)),
                endDate = Timestamp(Date(endMillis)),
            )

        // When
        val result = mapper.getEntity(model)

        // Then
        assertEquals(model.memberId, result.memberId)
        assertEquals(startMillis, result.startDateMillis)
        assertEquals(endMillis, result.endDateMillis)
        assertEquals("${model.memberId}-${model.startDate}", result.attendanceId)
    }
}

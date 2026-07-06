package cv.domain.mappers

import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.enums.DateFormatType
import cv.domain.repositories.DateProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AttendancePresentationMapperTest {
    private lateinit var mapper: AttendancePresentationMapper
    private val dateProviderRepository: DateProviderRepository = mockk()

    @Before
    fun setUp() {
        mapper = AttendancePresentationMapperImp(dateProviderRepository)
    }

    @Test
    fun `getModel for AttendanceEntity should return correct AttendancePresentationModel`() {
        // Given
        val entity =
            AttendanceEntity(
                attendanceId = "id1",
                memberId = "member1",
                startDateMillis = 1000L,
                endDateMillis = 2000L,
            )

        every { dateProviderRepository.getYear(1000L) } returns 2023
        every { dateProviderRepository.getMonth(1000L) } returns 1
        every { dateProviderRepository.format(1000L, DateFormatType.DATE_DAY) } returns "01"
        every { dateProviderRepository.format(1000L, DateFormatType.TIME) } returns "10:00"
        every { dateProviderRepository.format(2000L, DateFormatType.TIME) } returns "11:00"
        every { dateProviderRepository.activeStatusDuration(1000L, 2000L) } returns "1h"

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals("id1", result.attendanceId)
        assertEquals("2023", result.startYear)
        assertEquals("1", result.startMonth)
        assertEquals("member1", result.memberId)
        assertEquals("01", result.startDateDay)
        assertEquals("10:00", result.startTime)
        assertEquals("11:00", result.endTime)
        assertEquals("1h", result.activeStatusDuration)
    }

    @Test
    fun `getModel for AttendanceMonthEntity should return correct AttendanceMonthPresentationModel`() {
        // Given
        val attendanceEntity =
            AttendanceEntity(
                attendanceId = "id1",
                memberId = "member1",
                startDateMillis = 1000L,
                endDateMillis = 2000L,
            )
        val monthEntity =
            AttendanceMonthEntity(
                monthNumber = 1,
                totalMinutes = 60,
                attendances = listOf(attendanceEntity),
            )

        every { dateProviderRepository.getMonthName(1) } returns "January"
        every { dateProviderRepository.activeStatusDuration(60) } returns "1h"

        // Mocking the inner call to getModel(attendanceEntity)
        every { dateProviderRepository.getYear(1000L) } returns 2023
        every { dateProviderRepository.getMonth(1000L) } returns 1
        every { dateProviderRepository.format(1000L, DateFormatType.DATE_DAY) } returns "01"
        every { dateProviderRepository.format(1000L, DateFormatType.TIME) } returns "10:00"
        every { dateProviderRepository.format(2000L, DateFormatType.TIME) } returns "11:00"
        every { dateProviderRepository.activeStatusDuration(1000L, 2000L) } returns "1h"

        // When
        val result = mapper.getModel(monthEntity)

        // Then
        assertEquals(1, result.monthNumber)
        assertEquals("January", result.monthName)
        assertEquals(60, result.totalMinutes)
        assertEquals("1h", result.activeDuration)
        assertEquals(1, result.attendances.size)
        assertEquals("id1", result.attendances[0].attendanceId)
    }
}

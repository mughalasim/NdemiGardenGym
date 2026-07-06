package cv.domain.mappers

import cv.domain.entities.MemberEntity
import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.enums.MemberType
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class MemberPresentationMapperTest {
    private lateinit var mapper: MemberPresentationMapper
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()

    @Before
    fun setUp() {
        mapper = MemberPresentationMapperImp(dateProviderRepository, numberFormatUseCase)
    }

    @Test
    fun `getModel should return correct MemberPresentationModel`() {
        // Given
        val entity =
            MemberEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                activeNowDateMillis = 1000L,
                amountDue = 100.0,
                renewalFutureDateMillis = 2000L,
                memberType = MemberType.MEMBER,
            )

        every { dateProviderRepository.toPaymentPlanDuration(2000L) } returns Pair("30 days", 1)
        every { dateProviderRepository.getDate() } returns Date(5000L)
        every { dateProviderRepository.activeStatusDuration(1000L, 5000L) } returns "4s"
        every { numberFormatUseCase.getCurrencyFormatted(100.0) } returns "$100"

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals("John Doe", result.fullName)
        assertEquals("4s", result.lastActive)
        assertEquals("$100", result.amountDue)
        assertEquals("30 days", result.membershipRenewalDate)
        assertEquals(1, result.membershipWarningLevel)
    }

    @Test
    fun `getEditModel should return correct MemberEditPresentationModel`() {
        // Given
        val entity =
            MemberEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                registrationDateMillis = 1000L,
                height = 180.0,
            )

        every { dateProviderRepository.format(1000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-01-2023"
        every { numberFormatUseCase.getHeight(180.0) } returns 180.0
        every { numberFormatUseCase.getHeightUnit() } returns HeightUnit.CENTIMETERS

        // When
        val result = mapper.getEditModel(entity)

        // Then
        assertEquals("John", result.firstName)
        assertEquals("01-01-2023", result.registrationDate)
        assertEquals("180.0", result.height)
        assertEquals("Centimeters", result.heightUnit)
    }

    @Test
    fun `getDashboardModel should return correct MemberDashboardPresentationModel`() {
        // Given
        val entity =
            MemberEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                height = 180.0,
                activeNowDateMillis = 1000L,
            )
        val weights = listOf(WeightEntity(weight = 80.0))

        every { dateProviderRepository.getDate() } returns Date(5000L)
        every { dateProviderRepository.activeStatusDuration(1000L, 5000L) } returns "4s"
        every { numberFormatUseCase.getCurrencyFormatted(any()) } returns "$0"
        every { dateProviderRepository.format(any(), DateFormatType.DAY_MONTH_YEAR) } returns "01-01-2023"
        every { numberFormatUseCase.getWeight(80.0) } returns 80.0
        every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS
        every { numberFormatUseCase.getHeight(180.0) } returns 180.0
        every { numberFormatUseCase.getHeightUnit() } returns HeightUnit.CENTIMETERS
        every { numberFormatUseCase.getBMI(any(), 180.0) } returns 24.7

        // When
        val result = mapper.getDashboardModel(entity, weights, workouts = 10)

        // Then
        assertEquals("John Doe", result.fullName)
        assertEquals("80.0", result.weight)
        assertEquals("Kg", result.weightUnit)
        assertEquals("24.7", result.bmiValue.toString(), "24.7")
        assertEquals("10", result.workouts)
    }
}

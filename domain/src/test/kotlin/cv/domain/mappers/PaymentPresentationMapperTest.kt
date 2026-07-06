package cv.domain.mappers

import cv.domain.entities.PaymentEntity
import cv.domain.enums.DateFormatType
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PaymentPresentationMapperTest {
    private lateinit var mapper: PaymentPresentationMapper
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()

    @Before
    fun setUp() {
        mapper = PaymentPresentationMapperImp(dateProviderRepository, numberFormatUseCase)
    }

    @Test
    fun `getModel should return correct PaymentPresentationModel`() {
        // Given
        val entity =
            PaymentEntity(
                paymentId = "pay1",
                memberId = "member1",
                startDateMillis = 1000L,
                endDateMillis = 2000L,
                amount = 50.0,
            )

        every { dateProviderRepository.toPaymentPlanDuration(2000L) } returns Pair("Monthly", 1)
        every { dateProviderRepository.getYear(1000L) } returns 2023
        every { dateProviderRepository.format(1000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-01-2023"
        every { dateProviderRepository.format(2000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-02-2023"
        every { numberFormatUseCase.getCurrencyFormatted(50.0) } returns "$50"

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals("pay1", result.paymentId)
        assertEquals("member1", result.memberId)
        assertEquals("2023", result.startYear)
        assertEquals("$50", result.amount)
        assertEquals("Monthly", result.paymentPlanDuration)
        assertEquals(1, result.paymentPlanWarningLevel)
    }
}

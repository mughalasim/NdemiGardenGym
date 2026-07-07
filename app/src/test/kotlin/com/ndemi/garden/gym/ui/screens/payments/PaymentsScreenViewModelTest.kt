package com.ndemi.garden.gym.ui.screens.payments

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.PaymentYearEntity
import cv.domain.mappers.PaymentPresentationMapper
import cv.domain.presentationModels.PaymentPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.SettingsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentsScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk()
    private val paymentUseCase: PaymentUseCase = mockk()
    private val permissionsUseCase: PermissionsUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val settingsUseCase: SettingsUseCase = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()
    private val paymentPresentationMapper: PaymentPresentationMapper = mockk()
    private val dateProviderRepository: DateProviderRepository = mockk()

    private lateinit var viewModel: PaymentsScreenViewModel
    private val memberId = "member1"

    @Before
    fun setUp() {
        every { dateProviderRepository.getYear() } returns 2023
        every { settingsUseCase.observeSettingsChanged() } returns emptyFlow()
        every { numberFormatUseCase.getCurrencyFormatted(any()) } returns "$0.00"
        every { paymentUseCase.getPaymentPlanForMember(memberId, 2023) } returns
            flowOf(
                DomainResult.Success(PaymentYearEntity(payments = emptyList(), totalAmount = 0.0, canAddNewPayment = true)),
            )

        viewModel =
            PaymentsScreenViewModel(
                memberId,
                0,
                showSnackbar,
                jobRepository,
                converter,
                paymentUseCase,
                permissionsUseCase,
                navigationService,
                settingsUseCase,
                numberFormatUseCase,
                paymentPresentationMapper,
                dateProviderRepository,
            )
    }

    @Test
    fun `initial state should load payments`() {
        verify { checkNotNull(paymentUseCase.getPaymentPlanForMember(memberId, 2023)) }
    }

    @Test
    fun `increaseYear should increment year and reload`() {
        // Given
        every { paymentUseCase.getPaymentPlanForMember(memberId, 2024) } returns
            flowOf(
                DomainResult.Success(PaymentYearEntity(payments = emptyList(), totalAmount = 0.0, canAddNewPayment = true)),
            )

        // When
        viewModel.increaseYear()

        // Then
        assertEquals(2024, viewModel.selectedYear.value)
        verify { checkNotNull(paymentUseCase.getPaymentPlanForMember(memberId, 2024)) }
    }

    @Test
    fun `deletePayment should call usecase and reload`() =
        runTest {
            // Given
            val model = PaymentPresentationModel(paymentId = "p1")
            coEvery { paymentUseCase.deletePaymentPlanForMember(model) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_deleted) } returns "Deleted"
            every { paymentUseCase.getPaymentPlanForMember(memberId, 2023) } returns
                flowOf(
                    DomainResult.Success(PaymentYearEntity(payments = emptyList(), totalAmount = 0.0, canAddNewPayment = true)),
                )

            // When
            viewModel.deletePayment(model)

            // Then
            verify { showSnackbar(any()) }
            verify { checkNotNull(paymentUseCase.getPaymentPlanForMember(memberId, 2023)) }
        }
}

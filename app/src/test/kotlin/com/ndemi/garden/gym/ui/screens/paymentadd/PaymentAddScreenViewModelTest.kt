package com.ndemi.garden.gym.ui.screens.paymentadd

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.enums.DateFormatType
import cv.domain.enums.unit.CurrencyUnit
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PaymentUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentAddScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk(relaxed = true)
    private val paymentUseCase: PaymentUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()

    private lateinit var viewModel: PaymentAddScreenViewModel
    private val memberId = "member1"

    @Before
    fun setUp() {
        val date = Date(1000L)
        every { dateProviderRepository.getDate() } returns date
        every { dateProviderRepository.format(1000L, DateFormatType.DAY_MONTH_YEAR) } returns "01-01-1970"
        every { numberFormatUseCase.getCurrencyUnit() } returns CurrencyUnit.KES

        viewModel =
            PaymentAddScreenViewModel(
                memberId,
                showSnackbar,
                converter,
                paymentUseCase,
                navigationService,
                dateProviderRepository,
                numberFormatUseCase,
            )
    }

    @Test
    fun `initial state should have start date set`() {
        assertEquals(1000L, viewModel.inputData.value.startDate)
        assertEquals(PaymentAddScreenViewModel.UiState.Waiting, viewModel.uiStateFlow.value)
    }

    @Test
    fun `setData with valid month and amount should set state to Ready`() {
        // When
        viewModel.setData(monthDuration = "1", inputType = PaymentAddScreenInputType.MONTH_DURATION)
        viewModel.setData(amount = "100", inputType = PaymentAddScreenInputType.AMOUNT)

        // Then
        assertEquals(1, viewModel.inputData.value.monthDuration)
        assertEquals(100, viewModel.inputData.value.amount)
        assertEquals(PaymentAddScreenViewModel.UiState.Ready, viewModel.uiStateFlow.value)
    }

    @Test
    fun `setData with invalid month should show error`() {
        // Given
        every { converter.getMessage(UiErrorType.INVALID_MONTH_DURATION) } returns "Invalid Month"

        // When
        viewModel.setData(monthDuration = "6", inputType = PaymentAddScreenInputType.MONTH_DURATION)

        // Then
        assertEquals(
            PaymentAddScreenViewModel.UiState.Error("Invalid Month", PaymentAddScreenInputType.MONTH_DURATION),
            viewModel.uiStateFlow.value,
        )
    }

    @Test
    fun `onPaymentAddTapped success should show snackbar and navigate back`() =
        runTest {
            // Given
            viewModel.setData(monthDuration = "1", inputType = PaymentAddScreenInputType.MONTH_DURATION)
            viewModel.setData(amount = "100", inputType = PaymentAddScreenInputType.AMOUNT)
            coEvery { paymentUseCase.addPaymentPlanForMember(memberId, 1000L, 1, 100.0) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_added) } returns "Success"

            // When
            viewModel.onPaymentAddTapped()

            // Then
            verify { showSnackbar(any()) }
            verify { navigationService.popBack() }
        }
}

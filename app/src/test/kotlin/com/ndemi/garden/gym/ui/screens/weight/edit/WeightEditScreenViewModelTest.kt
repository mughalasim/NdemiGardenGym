package com.ndemi.garden.gym.ui.screens.weight.edit

import android.app.Application
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.enums.DateFormatType
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.WeightUseCase
import cv.domain.validator.Validator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class WeightEditScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val application: Application = mockk()
    private val weightUseCase: WeightUseCase = mockk()
    private val converter: ErrorCodeConverter = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val weightPresentationMapper: WeightPresentationMapper = mockk()
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val weightValidator: Validator = mockk()

    private lateinit var viewModel: WeightEditScreenViewModel

    @Before
    fun setUp() {
        val initialDate = 1000L
        every { dateProviderRepository.getDate() } returns Date(initialDate)
        every { weightValidator.isNotValid(any()) } returns false
        every { dateProviderRepository.isAfterNow(any()) } returns false
        every { weightPresentationMapper.getEditModel(any()) } returns WeightEditPresentationModel(dateMillis = initialDate)

        viewModel =
            WeightEditScreenViewModel(
                weightId = "w1",
                weight = "70.0",
                dateMillis = initialDate,
                showSnackbar = showSnackbar,
                application = application,
                weightUseCase = weightUseCase,
                converter = converter,
                navigationService = navigationService,
                weightPresentationMapper = weightPresentationMapper,
                dateProviderRepository = dateProviderRepository,
                weightValidator = weightValidator,
            )
    }

    @Test
    fun `initial state should have correct model and validate`() {
        assertEquals(1000L, viewModel.weightState.value.model.dateMillis)
        assertTrue(viewModel.weightState.value.updateEnabled)
    }

    @Test
    fun `onWeightValueChanged should update state and validate`() {
        // Given
        val newWeight = "invalid"
        every { weightValidator.isNotValid(newWeight) } returns true
        every { application.getString(R.string.error_invalid_weight) } returns "Invalid Weight"

        // When
        viewModel.onWeightValueChanged(newWeight)

        // Then
        assertEquals(newWeight, viewModel.weightState.value.model.formattedWeight)
        assertEquals("Invalid Weight", viewModel.weightState.value.errorWeight)
        assertFalse(viewModel.weightState.value.updateEnabled)
    }

    @Test
    fun `onDateSelected should update state and validate`() {
        // Given
        val newDate = 2000L
        every { dateProviderRepository.format(newDate, DateFormatType.DAY_MONTH_YEAR) } returns "02-01-2023"
        every { dateProviderRepository.isAfterNow(newDate) } returns true
        every { application.getString(R.string.error_invalid_future_date) } returns "Future Date"

        // When
        viewModel.onDateSelected(newDate)

        // Then
        assertEquals(newDate, viewModel.weightState.value.model.dateMillis)
        assertEquals("02-01-2023", viewModel.weightState.value.model.formattedDate)
        assertEquals("Future Date", viewModel.weightState.value.errorDate)
        assertFalse(viewModel.weightState.value.updateEnabled)
    }

    @Test
    fun `onAddTapped should call weightUseCase and navigate back on success`() =
        runTest {
            // Given
            coEvery { weightUseCase.setWeight(any()) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_added) } returns "Success"

            // When
            viewModel.onAddTapped()

            // Then
            verify { showSnackbar(any()) }
            verify { navigationService.popBack() }
        }
}

package com.ndemi.garden.gym.ui.screens.weight.list

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.WeightEntity
import cv.domain.enums.unit.WeightUnit
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.WeightUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeightListScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val weightPresentationMapper: WeightPresentationMapper = mockk()
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val weightUseCase: WeightUseCase = mockk()
    private val converter: ErrorCodeConverter = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()

    private lateinit var viewModel: WeightListScreenViewModel

    @Before
    fun setUp() {
        every { dateProviderRepository.getYear() } returns 2023
        every { weightUseCase.getWeightForYear(2023) } returns flowOf(DomainResult.Success(emptyList()))

        viewModel =
            WeightListScreenViewModel(
                showSnackbar,
                jobRepository,
                weightPresentationMapper,
                dateProviderRepository,
                navigationService,
                weightUseCase,
                converter,
                numberFormatUseCase,
            )
    }

    @Test
    fun `initial state should load weight list for current year`() {
        assertEquals(2023, viewModel.selectedYear.value)
        verify { weightUseCase.getWeightForYear(2023) }
    }

    @Test
    fun `increaseYear should increment year and reload list`() {
        // Given
        every { weightUseCase.getWeightForYear(2024) } returns flowOf(DomainResult.Success(emptyList()))

        // When
        viewModel.increaseYear()

        // Then
        assertEquals(2024, viewModel.selectedYear.value)
        verify { weightUseCase.getWeightForYear(2024) }
    }

    @Test
    fun `decreaseYear should decrement year and reload list`() {
        // Given
        every { weightUseCase.getWeightForYear(2022) } returns flowOf(DomainResult.Success(emptyList()))

        // When
        viewModel.decreaseYear()

        // Then
        assertEquals(2022, viewModel.selectedYear.value)
        verify { weightUseCase.getWeightForYear(2022) }
    }

    @Test
    fun `deleteWeight should call weightUseCase and show success snackbar`() =
        runTest {
            // Given
            val weightId = "w1"
            coEvery { weightUseCase.deleteWeight(weightId) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_weight_deleted) } returns "Deleted"

            // When
            viewModel.deleteWeight(weightId)

            // Then
            verify { showSnackbar(any()) }
        }

    @Test
    fun `getWeightList success should update weight list and weight change`() =
        runTest {
            // Given
            val weights =
                listOf(
                    WeightEntity(weight = 80.0),
                    WeightEntity(weight = 75.0),
                )
            val presentationModels =
                listOf(
                    WeightPresentationModel(weightValue = "80.0"),
                    WeightPresentationModel(weightValue = "75.0"),
                )
            every { weightUseCase.getWeightForYear(2023) } returns flowOf(DomainResult.Success(weights))
            every { weightPresentationMapper.getModel(weights[0]) } returns presentationModels[0]
            every { weightPresentationMapper.getModel(weights[1]) } returns presentationModels[1]
            every { numberFormatUseCase.getWeight(5.0) } returns 5.0
            every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS

            // When - triggering reload by calling init logic again or just checking if init already did it
            // Actually init was called in setUp. Let's recreate to be sure of the state update.
            viewModel =
                WeightListScreenViewModel(
                    showSnackbar,
                    jobRepository,
                    weightPresentationMapper,
                    dateProviderRepository,
                    navigationService,
                    weightUseCase,
                    converter,
                    numberFormatUseCase,
                )

            // Then
            assertEquals(presentationModels, viewModel.weightList.value)
            assertEquals("5.0 Kg", viewModel.weightChange.value)
        }
}

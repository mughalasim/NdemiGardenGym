package com.ndemi.garden.gym.ui.screens.weight.graph

import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import cv.domain.DomainResult
import cv.domain.entities.WeightEntity
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.WeightUseCase
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
class WeightGraphComponentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigationService: NavigationService = mockk(relaxed = true)
    private val weightUseCase: WeightUseCase = mockk()
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val dateProviderRepository: DateProviderRepository = mockk()

    private lateinit var viewModel: WeightGraphComponentViewModel

    @Before
    fun setUp() {
        every { dateProviderRepository.getYear() } returns 2023
        every { weightUseCase.getWeightForYear(2023) } returns flowOf(DomainResult.Success(emptyList()))

        viewModel =
            WeightGraphComponentViewModel(
                navigationService,
                weightUseCase,
                jobRepository,
                dateProviderRepository,
            )
    }

    @Test
    fun `initial state should load weight data for current year`() =
        runTest {
            // Given
            val weights = listOf(WeightEntity(weight = 80.0, dateMillis = 1000L))
            every { weightUseCase.getWeightForYear(2023) } returns flowOf(DomainResult.Success(weights))
            every { dateProviderRepository.getDayOfYear(1000L) } returns 1

            // Re-create to trigger init
            viewModel =
                WeightGraphComponentViewModel(
                    navigationService,
                    weightUseCase,
                    jobRepository,
                    dateProviderRepository,
                )

            // Then
            assertEquals(1, viewModel.weightDataList.value.size)
            assertEquals(1f, viewModel.weightDataList.value[0].date)
            assertEquals(80f, viewModel.weightDataList.value[0].weight)
        }

    @Test
    fun `onAddWeightTapped should navigate to weight list screen`() {
        // When
        viewModel.onAddWeightTapped()

        // Then
        verify { navigationService.open(any()) }
    }
}

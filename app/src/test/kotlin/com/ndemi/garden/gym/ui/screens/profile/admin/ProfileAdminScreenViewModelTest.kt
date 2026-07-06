package com.ndemi.garden.gym.ui.screens.profile.admin

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AdminDashboardUseCase
import cv.domain.usecase.SettingsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileAdminScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val adminDashboardUseCase: AdminDashboardUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val settingsUseCase: SettingsUseCase = mockk()
    private val converter: ErrorCodeConverter = mockk()
    private val dateProviderRepository: DateProviderRepository = mockk()

    private lateinit var viewModel: ProfileAdminScreenViewModel

    @Before
    fun setUp() {
        val date = Date(2023, 0, 1) // Jan 1 2023
        every { dateProviderRepository.getDate() } returns date
        every { settingsUseCase.observeSettingsChanged() } returns emptyFlow()
        val dashboardModel = AdminDashboardPresentationModel()
        every { adminDashboardUseCase.invoke(any()) } returns flowOf(dashboardModel)

        viewModel =
            ProfileAdminScreenViewModel(
                showSnackbar,
                jobRepository,
                adminDashboardUseCase,
                navigationService,
                settingsUseCase,
                converter,
                dateProviderRepository,
            )
    }

    @Test
    fun `initial state should fetch admin dashboard and update state to Success`() =
        runTest {
            assertTrue(viewModel.uiStateFlow.value is ProfileAdminScreenViewModel.UiState.Success)
        }

    @Test
    fun `onYearPlusTapped should increase year and reload`() {
        // Given
        every { converter.getString(R.string.txt_increased_year) } returns "Increased Year"

        // When
        viewModel.onYearPlusTapped()

        // Then
        verify { adminDashboardUseCase.invoke(any()) }
        verify { showSnackbar(any()) }
    }
}

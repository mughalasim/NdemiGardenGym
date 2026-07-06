package com.ndemi.garden.gym.ui.screens.main

import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AuthUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val navigationService: NavigationService = mockk()
    private val authUseCase: AuthUseCase = mockk()
    private val accessUseCase: AccessUseCase = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk(relaxed = true)

    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setUp() {
        coEvery { authUseCase.getAppVersion() } returns flowOf(DomainResult.Error<String>(cv.domain.enums.DomainErrorType.NO_DATA))
        coEvery { authUseCase.getAuthState() } returns flowOf(DomainResult.Error<Unit>(cv.domain.enums.DomainErrorType.UNAUTHORISED))
        every { navigationService.getInitialRoute() } returns Route.LoginScreen
        every { navigationService.getBottomNavItems() } returns emptyList()

        viewModel =
            MainScreenViewModel(
                showSnackbar,
                jobRepository,
                navigationService,
                authUseCase,
                accessUseCase,
                converter,
            )
    }

    @Test
    fun `initial state should be Ready for Login when unauthorized`() =
        runTest {
            assertTrue(viewModel.uiState.value is MainScreenViewModel.UiState.Ready)
            val state = viewModel.uiState.value as MainScreenViewModel.UiState.Ready
            assertEquals(Route.LoginScreen, state.initialRoute)
        }

    @Test
    fun `when authorized and member authenticated, state should be Ready`() =
        runTest {
            // Given
            val member = MemberEntity(id = "1", emailVerified = true)
            coEvery { authUseCase.getAuthState() } returns flowOf(DomainResult.Success(Unit))
            coEvery { authUseCase.getLoggedInUser() } returns flowOf(DomainResult.Success(member))

            // Re-create to trigger init block with new mocks
            viewModel =
                MainScreenViewModel(
                    showSnackbar,
                    jobRepository,
                    navigationService,
                    authUseCase,
                    accessUseCase,
                    converter,
                )

            // Then
            assertTrue(viewModel.uiState.value is MainScreenViewModel.UiState.Ready)
        }

    @Test
    fun `onLogOutTapped should call accessUseCase logOut`() {
        // When
        viewModel.onLogOutTapped()

        // Then
        verify { accessUseCase.logOut() }
    }
}

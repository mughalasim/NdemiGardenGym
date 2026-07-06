package com.ndemi.garden.gym.ui.screens.profile.member

import android.app.Application
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.presentationModels.MemberDashboardPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.SettingsUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.usecase.WeightUseCase
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
class ProfileMemberScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val application: Application = mockk()
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val authUseCase: AuthUseCase = mockk()
    private val memberUseCase: MemberUseCase = mockk()
    private val converter: ErrorCodeConverter = mockk()
    private val storageUseCase: StorageUseCase = mockk()
    private val attendanceUseCase: AttendanceUseCase = mockk()
    private val weightUseCase: WeightUseCase = mockk()
    private val settingsUseCase: SettingsUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val memberPresentationMapper: MemberPresentationMapper = mockk()

    private lateinit var viewModel: ProfileMemberScreenViewModel

    @Before
    fun setUp() {
        val member = MemberEntity(id = "1", firstName = "John")
        val memberFlow = kotlinx.coroutines.flow.MutableSharedFlow<MemberEntity>(replay = 1)
        memberFlow.tryEmit(member)
        every { authUseCase.observeUser() } returns memberFlow
        every { dateProviderRepository.getYear() } returns 2023
        every { attendanceUseCase.getMemberAttendancesForId("1", 2023) } returns flowOf(DomainResult.Success(emptyList()))
        every { weightUseCase.getWeightForYear(2023) } returns flowOf(DomainResult.Success(emptyList()))
        every { settingsUseCase.observeSettingsChanged() } returns emptyFlow()

        val dashboardModel = MemberDashboardPresentationModel(id = "1", fullName = "John Doe")
        every { memberPresentationMapper.getDashboardModel(any(), any(), any()) } returns dashboardModel

        viewModel =
            ProfileMemberScreenViewModel(
                showSnackbar,
                application,
                jobRepository,
                authUseCase,
                memberUseCase,
                converter,
                storageUseCase,
                attendanceUseCase,
                weightUseCase,
                settingsUseCase,
                navigationService,
                dateProviderRepository,
                memberPresentationMapper,
            )
    }

    @Test
    fun `initial state should fetch data and update UI state`() =
        runTest {
            // combine flow might take a moment to emit
            assertEquals(
                ProfileMemberScreenViewModel.UiState.Success(MemberDashboardPresentationModel(id = "1", fullName = "John Doe")),
                viewModel.uiStateFlow.value,
            )
        }

    @Test
    fun `onSettingsTapped should navigate to settings`() {
        // When
        viewModel.onSettingsTapped()

        // Then
        verify { navigationService.open(any()) }
    }
}

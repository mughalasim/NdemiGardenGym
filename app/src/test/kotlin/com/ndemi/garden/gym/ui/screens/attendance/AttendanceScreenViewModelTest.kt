package com.ndemi.garden.gym.ui.screens.attendance

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.presentationModels.AttendancePresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.PermissionsUseCase
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
class AttendanceScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk()
    private val attendanceUseCase: AttendanceUseCase = mockk()
    private val permissionsUseCase: PermissionsUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val dateProviderRepository: DateProviderRepository = mockk()

    private lateinit var viewModel: AttendanceScreenViewModel
    private val memberId = "member1"

    @Before
    fun setUp() {
        every { dateProviderRepository.getYear() } returns 2023
        every { attendanceUseCase.getMemberAttendancesForId(memberId, 2023) } returns flowOf(DomainResult.Success(emptyList()))

        viewModel =
            AttendanceScreenViewModel(
                memberId,
                showSnackbar,
                jobRepository,
                converter,
                attendanceUseCase,
                permissionsUseCase,
                navigationService,
                dateProviderRepository,
            )
    }

    @Test
    fun `initial state should load attendances`() {
        verify { attendanceUseCase.getMemberAttendancesForId(memberId, 2023) }
    }

    @Test
    fun `increaseYear should increment year and reload`() {
        // Given
        every { attendanceUseCase.getMemberAttendancesForId(memberId, 2024) } returns flowOf(DomainResult.Success(emptyList()))

        // When
        viewModel.increaseYear()

        // Then
        assertEquals(2024, viewModel.selectedYear.value)
        verify { attendanceUseCase.getMemberAttendancesForId(memberId, 2024) }
    }

    @Test
    fun `deleteAttendance should call usecase and show success snackbar`() =
        runTest {
            // Given
            val model = AttendancePresentationModel(attendanceId = "a1", startYear = "2023", startMonth = "1")
            coEvery { attendanceUseCase.deleteAttendance("2023", "1", "a1") } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_deleted) } returns "Deleted"
            every { attendanceUseCase.getMemberAttendancesForId(memberId, 2023) } returns flowOf(DomainResult.Success(emptyList()))

            // When
            viewModel.deleteAttendance(model)

            // Then
            verify { showSnackbar(any()) }
            verify { attendanceUseCase.getMemberAttendancesForId(memberId, 2023) }
        }
}

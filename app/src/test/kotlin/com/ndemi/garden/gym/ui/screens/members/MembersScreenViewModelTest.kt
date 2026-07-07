package com.ndemi.garden.gym.ui.screens.members

import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.presentationModels.MemberPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PermissionsUseCase
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
class MembersScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val jobRepository: JobRepository = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk()
    private val memberUseCase: MemberUseCase = mockk()
    private val attendanceUseCase: AttendanceUseCase = mockk()
    private val permissionsUseCase: PermissionsUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val dateProviderRepository: DateProviderRepository = mockk()
    private val memberPresentationMapper: MemberPresentationMapper = mockk()

    private lateinit var viewModel: MembersScreenViewModel

    @Before
    fun setUp() {
        every { memberUseCase.getAllMembers() } returns flowOf(DomainResult.Success(emptyList()))

        viewModel =
            MembersScreenViewModel(
                showSnackbar,
                jobRepository,
                MemberScreenType.ALL_MEMBERS,
                converter,
                memberUseCase,
                attendanceUseCase,
                permissionsUseCase,
                navigationService,
                dateProviderRepository,
                memberPresentationMapper,
            )
    }

    @Test
    fun `initial state should load members`() {
        verify { checkNotNull(memberUseCase.getAllMembers()) }
    }

    @Test
    fun `getMembers success should update members list`() =
        runTest {
            // Given
            val members = listOf(MemberEntity(id = "1", firstName = "John"))
            val presentationModels = listOf(MemberPresentationModel(id = "1", fullName = "John Doe"))
            every { memberUseCase.getAllMembers() } returns flowOf(DomainResult.Success(members))
            every { memberPresentationMapper.getModel(members[0]) } returns presentationModels[0]

            // When
            viewModel.getMembers()

            // Then
            assertEquals(presentationModels, viewModel.members)
        }

    @Test
    fun `onSearchTextChanged should filter members`() =
        runTest {
            // Given
            val members =
                listOf(
                    MemberEntity(id = "1"),
                    MemberEntity(id = "2"),
                )
            val presentationModels =
                listOf(
                    MemberPresentationModel(id = "1", fullName = "John Doe"),
                    MemberPresentationModel(id = "2", fullName = "Jane Smith"),
                )
            every { memberUseCase.getAllMembers() } returns flowOf(DomainResult.Success(members))
            every { memberPresentationMapper.getModel(members[0]) } returns presentationModels[0]
            every { memberPresentationMapper.getModel(members[1]) } returns presentationModels[1]
            viewModel.getMembers()

            // When
            viewModel.onSearchTextChanged("Jane")

            // Then
            assertEquals(1, viewModel.members.size)
            assertEquals("Jane Smith", viewModel.members[0].fullName)
        }

    @Test
    fun `onMemberTapped should navigate to member edit screen`() {
        // Given
        val model = MemberPresentationModel(id = "1")

        // When
        viewModel.onMemberTapped(model)

        // Then
        verify { navigationService.open(any()) }
    }
}

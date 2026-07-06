package com.ndemi.garden.gym.ui.screens.memberedit

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.presentationModels.MemberEditPresentationModel
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.validator.RegisterScreenValidators
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

@OptIn(ExperimentalCoroutinesApi::class)
class MemberEditScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val memberUseCase: MemberUseCase = mockk()
    private val validators: RegisterScreenValidators = mockk()
    private val converter: ErrorCodeConverter = mockk()
    private val storageUseCase: StorageUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val permissionsUseCase: PermissionsUseCase = mockk()
    private val numberFormatUseCase: NumberFormatUseCase = mockk()
    private val memberPresentationMapper: MemberPresentationMapper = mockk()

    private lateinit var viewModel: MemberEditScreenViewModel
    private val memberId = "member1"

    @Before
    fun setUp() {
        val member = MemberEntity(id = memberId, firstName = "John", memberType = cv.domain.enums.MemberType.MEMBER)
        val editModel = MemberEditPresentationModel(id = memberId, firstName = "John", memberType = "MEMBER")

        coEvery { memberUseCase.getMemberById(memberId) } returns DomainResult.Success(member)
        every { memberPresentationMapper.getEditModel(member) } returns editModel
        every { validators.name } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.phoneNumber } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.height } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.apartmentNumber } returns mockk { every { isNotValid(any()) } returns false }

        viewModel =
            MemberEditScreenViewModel(
                showSnackbar,
                memberId,
                memberUseCase,
                validators,
                converter,
                storageUseCase,
                navigationService,
                permissionsUseCase,
                numberFormatUseCase,
                memberPresentationMapper,
            )
    }

    @Test
    fun `initial state should load member and set state to Waiting`() =
        runTest {
            io.mockk.coVerify { memberUseCase.getMemberById(memberId) }
            assertEquals("John", viewModel.memberModel.value.firstName)
            assertEquals(MemberEditScreenViewModel.UiState.Waiting, viewModel.uiStateFlow.value)
        }

    @Test
    fun `setString should update model and set state to ReadyToUpdate when changed`() {
        // When
        viewModel.setString("Jane", MemberEditScreenInputType.FIRST_NAME)

        // Then
        assertEquals("Jane", viewModel.memberModel.value.firstName)
        assertEquals(MemberEditScreenViewModel.UiState.ReadyToUpdate, viewModel.uiStateFlow.value)
    }

    @Test
    fun `onUpdateTapped success should show snackbar and navigate back`() =
        runTest {
            // Given
            viewModel.setString("Jane", MemberEditScreenInputType.FIRST_NAME)
            val member = MemberEntity(id = memberId, firstName = "John", memberType = cv.domain.enums.MemberType.MEMBER)
            coEvery { memberUseCase.getMemberById(memberId) } returns DomainResult.Success(member)
            coEvery { memberUseCase.updateMember(any(), any()) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_updated) } returns "Success"

            // When
            viewModel.onUpdateTapped()

            // Then
            verify { showSnackbar(any()) }
            verify { navigationService.popBack() }
        }
}

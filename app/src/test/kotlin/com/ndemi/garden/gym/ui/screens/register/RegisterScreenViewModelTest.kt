package com.ndemi.garden.gym.ui.screens.register

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.enums.RegisterScreenInputType
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.dispatchers.ScopeProvider
import cv.domain.enums.MemberUpdateType
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.validator.RegisterScreenValidators
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val scopeProvider: ScopeProvider = mockk()
    private val converter: ErrorCodeConverter = mockk(relaxed = true)
    private val accessUseCase: AccessUseCase = mockk()
    private val memberUseCase: MemberUseCase = mockk()
    private val navigationService: NavigationService = mockk()
    private val validators: RegisterScreenValidators = mockk()
    private val dateProviderRepository: DateProviderRepository = mockk()

    private lateinit var viewModel: RegisterScreenViewModel
    private val testScope = TestScope(mainDispatcherRule.testDispatcher)

    @Before
    fun setUp() {
        every { scopeProvider.io() } returns testScope
        every { validators.name } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.email } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.apartmentNumber } returns mockk { every { isNotValid(any()) } returns false }
        every { validators.password } returns mockk { every { isNotValid(any()) } returns false }

        viewModel =
            RegisterScreenViewModel(
                hidePassword = false,
                showSnackbar = showSnackbar,
                scope = scopeProvider,
                converter = converter,
                accessUseCase = accessUseCase,
                memberUseCase = memberUseCase,
                navigationService = navigationService,
                validators = validators,
                dateProviderRepository = dateProviderRepository,
            )
    }

    @Test
    fun `initial state should be Waiting`() {
        assertEquals(RegisterScreenViewModel.UiState.Waiting, viewModel.uiStateFlow.value)
    }

    @Test
    fun `onRegisterTapped should register and then update member`() =
        runTest {
            // Given
            val email = "test@example.com"
            val password = "password123"
            val memberId = "member123"

            viewModel.setString("John", RegisterScreenInputType.FIRST_NAME)
            viewModel.setString("Doe", RegisterScreenInputType.LAST_NAME)
            viewModel.setString(email, RegisterScreenInputType.EMAIL)
            viewModel.setString("A1", RegisterScreenInputType.APARTMENT_NUMBER)
            viewModel.setString(password, RegisterScreenInputType.PASSWORD)
            viewModel.setString(password, RegisterScreenInputType.CONFIRM_PASSWORD)

            coEvery { accessUseCase.register(email, password) } returns DomainResult.Success(memberId)
            every { dateProviderRepository.getDate() } returns Date(1000L)
            coEvery { memberUseCase.updateMember(any(), MemberUpdateType.REGISTRATION) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_registered) } returns "Success"

            // When
            viewModel.onRegisterTapped()

            // Then
            assertEquals(RegisterScreenViewModel.UiState.Success, viewModel.uiStateFlow.value)
            verify { showSnackbar(any()) }
        }

    @Test
    fun `navigateBack should call navigationService popBack`() {
        // Given
        every { navigationService.popBack() } returns Unit

        // When
        viewModel.navigateBack()

        // Then
        verify { navigationService.popBack() }
    }
}

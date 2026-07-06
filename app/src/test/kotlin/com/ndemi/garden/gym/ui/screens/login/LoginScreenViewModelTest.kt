package com.ndemi.garden.gym.ui.screens.login

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.enums.LoginScreenInputType
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.usecase.AccessUseCase
import cv.domain.validator.Validator
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
class LoginScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk()
    private val accessUseCase: AccessUseCase = mockk()
    private val emailValidator: Validator = mockk()
    private val passwordValidator: Validator = mockk()

    private lateinit var viewModel: LoginScreenViewModel

    @Before
    fun setUp() {
        viewModel =
            LoginScreenViewModel(
                showSnackbar,
                converter,
                accessUseCase,
                emailValidator,
                passwordValidator,
            )
    }

    @Test
    fun `initial state should be Waiting`() {
        assertEquals(LoginScreenViewModel.UiState.Waiting, viewModel.uiStateFlow.value)
    }

    @Test
    fun `setString should update email and validate`() {
        // Given
        val email = "test@example.com"
        every { emailValidator.isNotValid(email) } returns false
        every { passwordValidator.isNotValid("") } returns true
        every { converter.getMessage(UiErrorType.INVALID_PASSWORD) } returns "Invalid Password"

        // When
        viewModel.setString(email, LoginScreenInputType.EMAIL)

        // Then
        assertEquals(email, viewModel.inputData.value.email)
        assertEquals(LoginScreenViewModel.UiState.Error("Invalid Password", LoginScreenInputType.PASSWORD), viewModel.uiStateFlow.value)
    }

    @Test
    fun `setString should set state to Ready when all inputs are valid`() {
        // Given
        every { emailValidator.isNotValid(any()) } returns false
        every { passwordValidator.isNotValid(any()) } returns false

        // When
        viewModel.setString("test@example.com", LoginScreenInputType.EMAIL)
        viewModel.setString("password123", LoginScreenInputType.PASSWORD)

        // Then
        assertEquals(LoginScreenViewModel.UiState.Ready, viewModel.uiStateFlow.value)
    }

    @Test
    fun `onLoginTapped should call accessUseCase and show success snackbar on success`() =
        runTest {
            // Given
            val email = "test@example.com"
            val password = "password123"
            every { emailValidator.isNotValid(any()) } returns false
            every { passwordValidator.isNotValid(any()) } returns false
            viewModel.setString(email, LoginScreenInputType.EMAIL)
            viewModel.setString(password, LoginScreenInputType.PASSWORD)

            coEvery { accessUseCase.login(email, password) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_successfully_logged_in) } returns "Success"

            // When
            viewModel.onLoginTapped()

            // Then
            assertEquals(LoginScreenViewModel.UiState.Success, viewModel.uiStateFlow.value)
            verify { showSnackbar(any()) }
        }

    @Test
    fun `onLoginTapped should show error snackbar on failure`() =
        runTest {
            // Given
            val email = "test@example.com"
            val password = "password123"
            every { emailValidator.isNotValid(any()) } returns false
            every { passwordValidator.isNotValid(any()) } returns false
            viewModel.setString(email, LoginScreenInputType.EMAIL)
            viewModel.setString(password, LoginScreenInputType.PASSWORD)

            coEvery { accessUseCase.login(email, password) } returns DomainResult.Error(cv.domain.enums.DomainErrorType.UNKNOWN)
            every { converter.getMessage(cv.domain.enums.DomainErrorType.UNKNOWN) } returns "Error"

            // When
            viewModel.onLoginTapped()

            // Then
            assertEquals(LoginScreenViewModel.UiState.Ready, viewModel.uiStateFlow.value)
            verify { showSnackbar(any()) }
        }
}

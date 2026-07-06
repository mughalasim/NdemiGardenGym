package com.ndemi.garden.gym.ui.screens.reset

import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
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
class ResetPasswordScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val converter: ErrorCodeConverter = mockk()
    private val accessUseCase: AccessUseCase = mockk()
    private val emailValidator: Validator = mockk()

    private lateinit var viewModel: ResetPasswordScreenViewModel

    @Before
    fun setUp() {
        viewModel =
            ResetPasswordScreenViewModel(
                showSnackbar,
                converter,
                accessUseCase,
                emailValidator,
            )
    }

    @Test
    fun `initial state should be Waiting`() {
        assertEquals(ResetPasswordScreenViewModel.UiState.Waiting, viewModel.uiStateFlow.value)
    }

    @Test
    fun `setEmail should update email and validate`() {
        // Given
        val email = "test@example.com"
        every { emailValidator.isNotValid(email) } returns false

        // When
        viewModel.setEmail(email)

        // Then
        assertEquals(email, viewModel.inputData.value)
        assertEquals(ResetPasswordScreenViewModel.UiState.Ready, viewModel.uiStateFlow.value)
    }

    @Test
    fun `setEmail should show error when email is invalid`() {
        // Given
        val email = "invalid"
        every { emailValidator.isNotValid(email) } returns true
        every { converter.getMessage(UiErrorType.INVALID_EMAIL) } returns "Invalid Email"

        // When
        viewModel.setEmail(email)

        // Then
        assertEquals(ResetPasswordScreenViewModel.UiState.Error("Invalid Email"), viewModel.uiStateFlow.value)
    }

    @Test
    fun `onResetPasswordTapped should call accessUseCase and show success snackbar on success`() =
        runTest {
            // Given
            val email = "test@example.com"
            every { emailValidator.isNotValid(email) } returns false
            viewModel.setEmail(email)

            coEvery { accessUseCase.resetPasswordForEmail(email) } returns DomainResult.Success(Unit)
            every { converter.getString(R.string.txt_email_successfully_sent) } returns "Success"

            // When
            viewModel.onResetPasswordTapped()

            // Then
            assertEquals(ResetPasswordScreenViewModel.UiState.Success(email), viewModel.uiStateFlow.value)
            verify { showSnackbar(any()) }
        }
}

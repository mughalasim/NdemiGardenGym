package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.appSnackbar.buildErrorSnackbar
import com.ndemi.garden.gym.ui.appSnackbar.buildSuccessSnackbar
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.usecase.AccessUseCase
import cv.domain.validator.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordScreenViewModel(
    private val showSnackbar: (AppSnackbarData) -> Unit,
    private val converter: ErrorCodeConverter,
    private val accessUseCase: AccessUseCase,
    private val emailValidator: Validator,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private val _inputData: MutableStateFlow<String> = MutableStateFlow("")
    val inputData: StateFlow<String> = _inputData

    fun setEmail(email: String) {
        _inputData.value = email
        validateInput()
    }

    private fun validateInput() {
        if (emailValidator.isNotValid(_inputData.value)) {
            sendAction(
                Action.ShowInputError(converter.getMessage(UiErrorType.INVALID_EMAIL)),
            )
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onResetPasswordTapped() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            accessUseCase.resetPasswordForEmail(_inputData.value).also {
                when (it) {
                    is DomainResult.Error -> {
                        showSnackbar(buildErrorSnackbar(converter.getMessage(it.error)))
                        sendAction(Action.SetReady)
                    }

                    is DomainResult.Success -> {
                        showSnackbar(buildSuccessSnackbar(converter.getString(R.string.txt_email_successfully_sent)))
                        sendAction(Action.Success(_inputData.value))
                    }
                }
            }
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(
            val message: String,
        ) : UiState

        data class Success(
            val email: String,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState): UiState = UiState.Ready
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowInputError(
            val message: String,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message)
        }

        data class Success(
            val email: String,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(email)
        }
    }
}

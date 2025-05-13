package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.usecase.AccessUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val accessUseCase: AccessUseCase,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private val _inputData: MutableStateFlow<String> = MutableStateFlow("")
    val inputData: StateFlow<String> = _inputData

    fun setEmail(email: String) {
        _inputData.value = email
        validateInput()
    }

    private fun validateInput() {
        if (_inputData.value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_inputData.value).matches()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_EMAIL),
                    InputType.EMAIL,
                ),
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
                    is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                    is DomainResult.Success -> sendAction(Action.Success(_inputData.value))
                }
            }
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(val message: String, val inputType: InputType) : UiState

        data class Success(val email: String) : UiState
    }

    enum class InputType {
        NONE,
        EMAIL,
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState): UiState = UiState.Ready
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(val message: String, val inputType: InputType = InputType.NONE) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }

        data class Success(val email: String) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(email)
        }
    }
}

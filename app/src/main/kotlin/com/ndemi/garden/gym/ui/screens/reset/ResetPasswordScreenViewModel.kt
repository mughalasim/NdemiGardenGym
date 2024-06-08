package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.usecase.AuthUseCase

class ResetPasswordScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private var email: String = ""

    fun setEmail(email: String) {
        this.email = email
        validateInput()
    }


    private fun validateInput(){
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            sendAction(Action.ShowError(
                converter.getMessage(UiError.EMAIL_INVALID),
                InputType.EMAIL)
            )
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onResetPasswordTapped() {
        sendAction(Action.SetLoading)
        authUseCase.resetPasswordForEmail(email){
            when(it){
                is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                is DomainResult.Success -> sendAction(Action.Success)
            }
        }
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(val message: String, val inputType: InputType) : UiState

        data object Success : UiState

    }

    enum class InputType {
        NONE,
        EMAIL
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

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}
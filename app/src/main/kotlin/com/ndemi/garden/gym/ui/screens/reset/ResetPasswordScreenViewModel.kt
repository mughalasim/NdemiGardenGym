package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.entities.ResponseEntity

class ResetPasswordScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
) : BaseViewModel<ResetPasswordScreenViewModel.UiState, ResetPasswordScreenViewModel.Action>(UiState.Waiting) {
    private var email: String = ""

    fun setEmail(email: String) {
        this.email = email
        validateInput()
    }


    private fun validateInput(){
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            sendAction(Action.ShowError(UiError.EMAIL_INVALID, errorCodeConverter))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onResetPasswordTapped() {
        sendAction(Action.SetLoading)
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(val uiError: UiError, val message: String) : UiState

        data class DataReceived(
            val responseEntity: ResponseEntity,
        ) : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState): UiState = UiState.Ready
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(
            val uiError: UiError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Error(uiError, errorCodeConverter.getMessage(uiError))
        }

        data class DataReceived(val responseEntity: ResponseEntity) : Action {
            override fun reduce(state: UiState): UiState = UiState.DataReceived(responseEntity)
        }
    }
}
package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.entities.ResponseEntity

class LoginScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
) :BaseViewModel<LoginScreenViewModel.UiState, LoginScreenViewModel.Action>(UiState.Waiting) {
    private var email: String = ""
    private var password: String = ""

    fun setEmail(email: String) {
        this.email = email
        validateInput()
    }

    fun setPassword(password: String) {
        this.password = password
        validateInput()
    }

    private fun validateInput(){
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            sendAction(Action.ShowError(UiError.EMAIL_INVALID, errorCodeConverter))
        } else if (password.isEmpty()){
            sendAction(Action.ShowError(UiError.PASSWORD_INVALID, errorCodeConverter))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onLoginTapped() {
        TODO("Not yet implemented")
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data class Error(val uiError: UiError, val message: String) : UiState

        data class DataReceived(
            val responseEntity: ResponseEntity,
        ) : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState): UiState = UiState.Ready
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
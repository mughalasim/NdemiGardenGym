package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.usecase.AuthUseCase

class LoginScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val navigationService: NavigationService,
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
            sendAction(Action.ShowError(converter.getMessage(UiError.EMAIL_INVALID), InputType.EMAIL))
        } else if (password.isEmpty()){
            sendAction(Action.ShowError(converter.getMessage(UiError.PASSWORD_INVALID), InputType.PASSWORD))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onLoginTapped() {
        sendAction(Action.SetLoading)
        authUseCase.login(email, password){
            if (it.isEmpty()){
                sendAction(Action.ShowError(converter.getMessage(UiError.INVALID_LOGIN_CREDENTIALS)))
            } else {
                sendAction(Action.Success(it))
            }
        }
    }

    fun navigateLogInSuccess() {
        navigationService.open(Route.ProfileScreen, true)
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(val message: String, val inputType: InputType) : UiState

        data class Success(val userId: String, ) : UiState

    }

    enum class InputType {
        NONE,
        EMAIL,
        PASSWORD
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

        data class Success(val userId: String) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(userId)
        }
    }
}

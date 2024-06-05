package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase

class RegisterScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<RegisterScreenViewModel.UiState, RegisterScreenViewModel.Action>(UiState.Waiting) {
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var firstName: String = ""
    private var lastName: String = ""

    fun setString(value: String, inPutType: InPutType) {
        when (inPutType) {
            InPutType.FIRST_NAME -> this.firstName = value
            InPutType.LAST_NAME -> this.lastName = value
            InPutType.EMAIL -> this.email = value
            InPutType.PASSWORD -> this.password = value
            InPutType.CONFIRM_PASSWORD -> this.confirmPassword = value
        }
        validateInput()
    }

    private fun validateInput() {
        if (firstName.isEmpty()) {
            sendAction(Action.ShowError(UiError.FIRST_NAME_INVALID, errorCodeConverter))

        } else if (lastName.isEmpty()) {
            sendAction(Action.ShowError(UiError.LAST_NAME_INVALID, errorCodeConverter))

        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            sendAction(Action.ShowError(UiError.EMAIL_INVALID, errorCodeConverter))

        } else if (password.isEmpty()) {
            sendAction(Action.ShowError(UiError.PASSWORD_INVALID, errorCodeConverter))

        } else if (confirmPassword.isEmpty()) {
            sendAction(Action.ShowError(UiError.PASSWORD_CONFIRM_INVALID, errorCodeConverter))

        } else if (password != confirmPassword) {
            sendAction(Action.ShowError(UiError.PASSWORD_MATCH_INVALID, errorCodeConverter))

        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onRegisterTapped() {
        sendAction(Action.SetLoading)
        authUseCase.register(MemberEntity("", firstName, lastName, email, email), password){
            if (it.isEmpty()){
                sendAction(Action.ShowError(UiError.REGISTRATION_FAILED, errorCodeConverter))
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

        data class Error(val uiError: UiError, val message: String) : UiState

        data class Success(
            val userId: String,
        ) : UiState

    }

    enum class InPutType {
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        PASSWORD,
        CONFIRM_PASSWORD
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

        data class Success(val userId: String) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(userId)
        }
    }
}

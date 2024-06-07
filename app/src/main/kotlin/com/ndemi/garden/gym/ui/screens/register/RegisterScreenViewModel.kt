package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class RegisterScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<RegisterScreenViewModel.UiState, RegisterScreenViewModel.Action>(UiState.Waiting) {
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var firstName: String = ""
    private var lastName: String = ""

    fun setString(value: String, inPutType: InputType) {
        when (inPutType) {
            InputType.FIRST_NAME -> this.firstName = value
            InputType.LAST_NAME -> this.lastName = value
            InputType.EMAIL -> this.email = value
            InputType.PASSWORD -> this.password = value
            InputType.CONFIRM_PASSWORD -> this.confirmPassword = value
            InputType.NONE -> Unit
        }
        validateInput()
    }

    private fun validateInput() {
        if (firstName.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.FIRST_NAME_INVALID),
                    InputType.FIRST_NAME
                )
            )

        } else if (lastName.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.LAST_NAME_INVALID),
                    InputType.LAST_NAME
                )
            )

        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.EMAIL_INVALID),
                    InputType.EMAIL
                )
            )

        } else if (password.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.PASSWORD_INVALID),
                    InputType.PASSWORD
                )
            )

        } else if (confirmPassword.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.PASSWORD_CONFIRM_INVALID),
                    InputType.CONFIRM_PASSWORD
                )
            )

        } else if (password != confirmPassword) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.PASSWORD_MATCH_INVALID),
                    InputType.CONFIRM_PASSWORD
                )
            )

        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onRegisterTapped() {
        sendAction(Action.SetLoading)
        authUseCase.register(email, password) {
            if (it.isEmpty()) {
                sendAction(Action.ShowError(converter.getMessage(UiError.REGISTRATION_FAILED)))
            } else {
                updateMember(it)
            }
        }
    }

    private fun updateMember(memberId: String){
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.updateMember(MemberEntity(
                id = memberId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                registrationDate = DateTime.now().toDate()
            )).also {result ->
                when(result){
                    is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(UiError.REGISTRATION_FAILED)))
                    is DomainResult.Success -> sendAction(Action.Success)
                }
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

        data object Success : UiState

    }

    enum class InputType {
        NONE,
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
            val message: String,
            val inputType: InputType = InputType.NONE
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Error(message, inputType)
        }

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}

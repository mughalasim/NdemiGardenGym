package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.isValidApartmentNumber
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.UUID

class RegisterScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var apartmentNumber: String = ""

    fun setString(value: String, inPutType: InputType) {
        when (inPutType) {
            InputType.FIRST_NAME -> this.firstName = value
            InputType.LAST_NAME -> this.lastName = value
            InputType.EMAIL -> this.email = value
            InputType.APARTMENT_NUMBER -> this.apartmentNumber = value
            InputType.PASSWORD -> this.password = value
            InputType.CONFIRM_PASSWORD -> this.confirmPassword = value
            InputType.NONE -> Unit
        }
        validateInput()
    }

    private fun validateInput() {
        if (firstName.isEmpty() || firstName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_FIRST_NAME),
                    InputType.FIRST_NAME
                )
            )

        } else if (lastName.isEmpty() || lastName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_LAST_NAME),
                    InputType.LAST_NAME
                )
            )

        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_EMAIL),
                    InputType.EMAIL
                )
            )

        } else if (password.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD),
                    InputType.PASSWORD
                )
            )

        } else if (confirmPassword.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD_CONFIRM),
                    InputType.CONFIRM_PASSWORD
                )
            )

        } else if (password != confirmPassword) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD_MATCH),
                    InputType.CONFIRM_PASSWORD
                )
            )

        } else if (apartmentNumber.isNotEmpty() && !apartmentNumber.isValidApartmentNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_APARTMENT_NUMBER),
                    InputType.APARTMENT_NUMBER
                )
            )

        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onRegisterTapped() {
        sendAction(Action.SetLoading)
        authUseCase.register(email, password) {
            when(it){
                is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                is DomainResult.Success -> updateMember(it.data)
            }
        }
    }

    fun onRegisterNewTapped(){
        sendAction(Action.SetLoading)
        updateMember(UUID.randomUUID().toString())
    }

    private fun updateMember(memberId: String){
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.updateMember(MemberEntity(
                id = memberId,
                firstName = firstName.replaceFirstChar(Char::uppercase).trim(),
                lastName = lastName.replaceFirstChar(Char::uppercase).trim(),
                email = email.trim(),
                registrationDate = DateTime.now().toDate(),
                apartmentNumber = apartmentNumber
            )).also {result ->
                when(result){
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(converter.getMessage(UiError.REGISTRATION_FAILED)))
                    is DomainResult.Success -> sendAction(Action.Success)
                }
            }
        }
    }

    fun navigateLogInSuccess() {
        navigationService.open(Route.ProfileScreen, true)
    }

    fun navigateBack() {
        navigationService.popBack()
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
        APARTMENT_NUMBER,
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

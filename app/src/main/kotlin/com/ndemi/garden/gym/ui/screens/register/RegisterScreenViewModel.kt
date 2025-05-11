package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.navigation.NavigationService
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
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.UUID

class RegisterScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
    private val hidePassword: Boolean,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    data class InputData(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = if (BuildConfig.DEBUG) BuildConfig.ADMIN_STAGING else "",
        val apartmentNumber: String = "",
        val password: String = "",
        val confirmPassword: String = "",
    )

    private val _inputData = MutableStateFlow(InputData())
    val inputData: StateFlow<InputData> = _inputData

    fun shouldHidePassword() = hidePassword

    fun setString(
        value: String,
        inPutType: InputType,
    ) {
        _inputData.value =
            when (inPutType) {
                InputType.FIRST_NAME -> _inputData.value.copy(firstName = value)
                InputType.LAST_NAME -> _inputData.value.copy(lastName = value)
                InputType.EMAIL -> _inputData.value.copy(email = value)
                InputType.APARTMENT_NUMBER -> _inputData.value.copy(apartmentNumber = value)
                InputType.PASSWORD -> _inputData.value.copy(password = value)
                InputType.CONFIRM_PASSWORD -> _inputData.value.copy(confirmPassword = value)
                InputType.NONE -> _inputData.value
            }
        validateInput()
    }

    private fun validateInput() {
        val email = _inputData.value.email
        val firstName = _inputData.value.firstName
        val lastName = _inputData.value.lastName
        val apartmentNumber = _inputData.value.apartmentNumber

        when {
            firstName.isEmpty() || firstName.isDigitsOnly() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_FIRST_NAME),
                        InputType.FIRST_NAME,
                    ),
                )
            }

            lastName.isEmpty() || lastName.isDigitsOnly() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_LAST_NAME),
                        InputType.LAST_NAME,
                    ),
                )
            }

            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_EMAIL),
                        InputType.EMAIL,
                    ),
                )
            }

            apartmentNumber.isNotEmpty() && !apartmentNumber.isValidApartmentNumber() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_APARTMENT_NUMBER),
                        InputType.APARTMENT_NUMBER,
                    ),
                )
            }

            shouldValidatePassword -> passwordCheck()

            else -> {
                sendAction(Action.SetReady)
            }
        }
    }

    private val shouldValidatePassword =
        !hidePassword && (
            _inputData.value.password.isEmpty() ||
                _inputData.value.confirmPassword.isEmpty() ||
                _inputData.value.password != _inputData.value.confirmPassword
        )

    private fun passwordCheck() {
        when {
            _inputData.value.password.isEmpty() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_PASSWORD),
                        InputType.PASSWORD,
                    ),
                )
            }

            _inputData.value.confirmPassword.isEmpty() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_PASSWORD_CONFIRM),
                        InputType.CONFIRM_PASSWORD,
                    ),
                )
            }

            _inputData.value.password != _inputData.value.confirmPassword -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiError.INVALID_PASSWORD_MATCH),
                        InputType.CONFIRM_PASSWORD,
                    ),
                )
            }

            else -> {
                sendAction(Action.SetReady)
            }
        }
    }

    fun onRegisterTapped() {
        sendAction(Action.SetLoading)
        authUseCase.register(
            inputData.value.email,
            inputData.value.password,
        ) {
            when (it) {
                is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                is DomainResult.Success -> updateMember(it.data, UpdateType.REGISTRATION)
            }
        }
    }

    fun onRegisterNewTapped() {
        updateMember(UUID.randomUUID().toString(), UpdateType.CREATE_MEMBER)
    }

    private fun updateMember(
        memberId: String,
        updateType: UpdateType,
    ) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase
                .updateMember(
                    MemberEntity(
                        id = memberId,
                        firstName =
                            (inputData.value.firstName)
                                .replaceFirstChar(Char::uppercase)
                                .trim(),
                        lastName =
                            (inputData.value.lastName)
                                .replaceFirstChar(Char::uppercase)
                                .trim(),
                        email =
                            (inputData.value.email)
                                .trim(),
                        registrationDateMillis = DateTime.now().millis,
                        apartmentNumber =
                            (inputData.value.apartmentNumber)
                                .replaceFirstChar(Char::uppercase),
                        profileImageUrl = "",
                    ),
                    updateType,
                ).also {
                    when (it) {
                        is DomainResult.Error ->
                            sendAction(Action.ShowError(converter.getMessage(it.error)))

                        is DomainResult.Success -> sendAction(Action.Success)
                    }
                }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(
            val message: String,
            val inputType: InputType,
        ) : UiState

        data object Success : UiState
    }

    enum class InputType {
        NONE,
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        APARTMENT_NUMBER,
        PASSWORD,
        CONFIRM_PASSWORD,
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
            val inputType: InputType = InputType.NONE,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}

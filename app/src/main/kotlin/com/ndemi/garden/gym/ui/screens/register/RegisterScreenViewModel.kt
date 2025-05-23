package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.RegisterScreenInputType
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.validator.MemberValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.UUID

class RegisterScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val accessUseCase: AccessUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
    private val hidePassword: Boolean,
    private val validators: MemberValidators,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    data class InputData(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val apartmentNumber: String = "",
        val password: String = "",
        val confirmPassword: String = "",
    )

    private val _inputData = MutableStateFlow(InputData())
    val inputData: StateFlow<InputData> = _inputData

    fun shouldHidePassword() = hidePassword

    fun setString(
        value: String,
        inPutType: RegisterScreenInputType,
    ) {
        _inputData.value =
            when (inPutType) {
                RegisterScreenInputType.FIRST_NAME -> _inputData.value.copy(firstName = value)
                RegisterScreenInputType.LAST_NAME -> _inputData.value.copy(lastName = value)
                RegisterScreenInputType.EMAIL -> _inputData.value.copy(email = value)
                RegisterScreenInputType.APARTMENT_NUMBER -> _inputData.value.copy(apartmentNumber = value)
                RegisterScreenInputType.PASSWORD -> _inputData.value.copy(password = value)
                RegisterScreenInputType.CONFIRM_PASSWORD -> _inputData.value.copy(confirmPassword = value)
                RegisterScreenInputType.NONE -> _inputData.value
            }
        validateInput()
    }

    private fun validateInput() {
        val email = _inputData.value.email
        when {
            validators.name.isNotValid(_inputData.value.firstName) -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_FIRST_NAME),
                        RegisterScreenInputType.FIRST_NAME,
                    ),
                )
            }

            validators.name.isNotValid(_inputData.value.lastName) -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_LAST_NAME),
                        RegisterScreenInputType.LAST_NAME,
                    ),
                )
            }

            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_EMAIL),
                        RegisterScreenInputType.EMAIL,
                    ),
                )
            }

            validators.apartmentNumber.isNotValid(_inputData.value.apartmentNumber) -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_APARTMENT_NUMBER),
                        RegisterScreenInputType.APARTMENT_NUMBER,
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
                        converter.getMessage(UiErrorType.INVALID_PASSWORD),
                        RegisterScreenInputType.PASSWORD,
                    ),
                )
            }

            _inputData.value.confirmPassword.isEmpty() -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_PASSWORD_CONFIRM),
                        RegisterScreenInputType.CONFIRM_PASSWORD,
                    ),
                )
            }

            _inputData.value.password != _inputData.value.confirmPassword -> {
                sendAction(
                    Action.ShowError(
                        converter.getMessage(UiErrorType.INVALID_PASSWORD_MATCH),
                        RegisterScreenInputType.CONFIRM_PASSWORD,
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
        viewModelScope.launch {
            accessUseCase.register(
                inputData.value.email,
                inputData.value.password,
            ).also {
                when (it) {
                    is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                    is DomainResult.Success -> updateMember(it.data, MemberUpdateType.REGISTRATION)
                }
            }
        }
    }

    fun onRegisterNewTapped() {
        updateMember(UUID.randomUUID().toString(), MemberUpdateType.CREATE)
    }

    private fun updateMember(
        memberId: String,
        memberUpdateType: MemberUpdateType,
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
                    memberUpdateType,
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
            val inputType: RegisterScreenInputType,
        ) : UiState

        data object Success : UiState
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
            val inputType: RegisterScreenInputType = RegisterScreenInputType.NONE,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}

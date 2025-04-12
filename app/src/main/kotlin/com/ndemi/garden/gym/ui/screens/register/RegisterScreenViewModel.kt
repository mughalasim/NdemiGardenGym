package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.UUID

class RegisterScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    data class InputData(
        val firstName: String,
        val lastName: String,
        val email: String,
        val apartmentNumber: String,
        val password: String,
        val confirmPassword: String,
    )

    private val _inputData =
        MutableLiveData(
            InputData(
                firstName = "",
                lastName = "",
                email = if (BuildConfig.DEBUG) BuildConfig.ADMIN_STAGING else "",
                apartmentNumber = "",
                password = "",
                confirmPassword = "",
            ),
        )
    val inputData: LiveData<InputData> = _inputData

    fun setString(
        value: String,
        inPutType: InputType,
    ) {
        _inputData.value =
            when (inPutType) {
                InputType.FIRST_NAME -> _inputData.value?.copy(firstName = value)
                InputType.LAST_NAME -> _inputData.value?.copy(lastName = value)
                InputType.EMAIL -> _inputData.value?.copy(email = value)
                InputType.APARTMENT_NUMBER -> _inputData.value?.copy(apartmentNumber = value)
                InputType.PASSWORD -> _inputData.value?.copy(password = value)
                InputType.CONFIRM_PASSWORD -> _inputData.value?.copy(confirmPassword = value)
                InputType.NONE -> _inputData.value
            }
        validateInput()
    }

    private fun validateInput() {
        val email = _inputData.value?.email.orEmpty()
        val password = _inputData.value?.password.orEmpty()
        val confirmPassword = _inputData.value?.confirmPassword.orEmpty()
        val firstName = _inputData.value?.firstName.orEmpty()
        val lastName = _inputData.value?.lastName.orEmpty()
        val apartmentNumber = _inputData.value?.apartmentNumber.orEmpty()

        if (firstName.isEmpty() || firstName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_FIRST_NAME),
                    InputType.FIRST_NAME,
                ),
            )
        } else if (lastName.isEmpty() || lastName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_LAST_NAME),
                    InputType.LAST_NAME,
                ),
            )
        } else if (email.isEmpty() ||
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_EMAIL),
                    InputType.EMAIL,
                ),
            )
        } else if (password.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD),
                    InputType.PASSWORD,
                ),
            )
        } else if (confirmPassword.isEmpty()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD_CONFIRM),
                    InputType.CONFIRM_PASSWORD,
                ),
            )
        } else if (password != confirmPassword) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PASSWORD_MATCH),
                    InputType.CONFIRM_PASSWORD,
                ),
            )
        } else if (apartmentNumber.isNotEmpty() && !apartmentNumber.isValidApartmentNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_APARTMENT_NUMBER),
                    InputType.APARTMENT_NUMBER,
                ),
            )
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onRegisterTapped() {
        sendAction(Action.SetLoading)
        authUseCase.register(
            inputData.value?.email.orEmpty(),
            inputData.value?.password.orEmpty(),
        ) {
            when (it) {
                is DomainResult.Error -> sendAction(Action.ShowError(converter.getMessage(it.error)))
                is DomainResult.Success -> updateMember(it.data, UpdateType.SELF_REGISTRATION)
            }
        }
    }

    fun onRegisterNewTapped() {
        updateMember(UUID.randomUUID().toString(), UpdateType.ADMIN_REGISTRATION)
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
                            (inputData.value?.firstName.orEmpty())
                                .replaceFirstChar(Char::uppercase)
                                .trim(),
                        lastName =
                            (inputData.value?.lastName.orEmpty())
                                .replaceFirstChar(Char::uppercase)
                                .trim(),
                        email =
                            (inputData.value?.email.orEmpty())
                                .trim(),
                        registrationDateMillis = DateTime.now().millis,
                        apartmentNumber =
                            (inputData.value?.apartmentNumber.orEmpty())
                                .replaceFirstChar(Char::uppercase),
                        profileImageUrl = "",
                    ),
                    updateType,
                ).also { result ->
                    when (result) {
                        is DomainResult.Error ->
                            sendAction(Action.ShowError(converter.getMessage(UiError.REGISTRATION_FAILED)))

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

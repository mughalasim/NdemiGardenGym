package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.usecase.AuthUseCase

class LoginScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    data class InputData(
        val email: String,
        val password: String,
    )

    private val _inputData =
        MutableLiveData(
            InputData(
                email = if (BuildConfig.DEBUG) BuildConfig.ADMIN_STAGING else "",
                password = "",
            ),
        )
    val inputData: LiveData<InputData> = _inputData

    fun setString(
        value: String,
        inputType: InputType,
    ) {
        _inputData.value =
            when (inputType) {
                InputType.NONE -> _inputData.value
                InputType.EMAIL -> _inputData.value?.copy(email = value)
                InputType.PASSWORD -> _inputData.value?.copy(password = value)
            }
        validateInput()
    }

    private fun validateInput() {
        val email = _inputData.value?.email.orEmpty()
        val password = _inputData.value?.password.orEmpty()

        if (email.isEmpty() ||
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            sendAction(Action.ShowError(converter.getMessage(UiError.INVALID_EMAIL), InputType.EMAIL))
        } else if (password.isEmpty()) {
            sendAction(Action.ShowError(converter.getMessage(UiError.INVALID_PASSWORD), InputType.PASSWORD))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onLoginTapped() {
        sendAction(Action.SetLoading)
        authUseCase.login(_inputData.value?.email.orEmpty(), _inputData.value?.password.orEmpty()) {
            when (it) {
                is DomainResult.Success ->
                    sendAction(Action.Success)

                is DomainResult.Error ->
                    sendAction(Action.ShowError(converter.getMessage(it.error)))
            }
        }
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
        EMAIL,
        PASSWORD,
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

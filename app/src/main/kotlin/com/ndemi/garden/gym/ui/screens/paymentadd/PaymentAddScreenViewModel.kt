package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType.AMOUNT
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType.MONTH_DURATION
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType.NONE
import com.ndemi.garden.gym.ui.enums.PaymentAddScreenInputType.START_DATE
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.PaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentAddScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val paymentUseCase: PaymentUseCase,
    private val navigationService: NavigationService,
    private val dateProviderRepository: DateProviderRepository,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private var memberId = ""

    data class InputData(
        val startDate: Long = 0,
        val startDateFormatted: String = "",
        val monthDuration: Int = 0,
        val amount: Int = 0,
    )

    private val _inputData =
        MutableStateFlow(
            InputData(startDateFormatted = dateProviderRepository.formatDayMonthYear(dateProviderRepository.getDate().time)),
        )
    val inputData: StateFlow<InputData> = _inputData

    fun setData(
        startDate: Long = _inputData.value.startDate,
        monthDuration: String = "",
        amount: String = "",
        inputType: PaymentAddScreenInputType,
    ) {
        _inputData.value =
            when (inputType) {
                NONE -> {
                    _inputData.value
                }

                START_DATE -> {
                    _inputData.value.copy(
                        startDate = startDate,
                        startDateFormatted = dateProviderRepository.formatDayMonthYear(startDate),
                    )
                }

                MONTH_DURATION -> {
                    if (monthDuration.isNotEmpty() && monthDuration.isDigitsOnly()) {
                        _inputData.value.copy(monthDuration = monthDuration.toInt())
                    } else {
                        _inputData.value.copy(monthDuration = 0)
                    }
                }

                AMOUNT -> {
                    if (amount.isNotEmpty() && amount.isDigitsOnly()) {
                        _inputData.value.copy(amount = amount.toInt())
                    } else {
                        _inputData.value.copy(amount = 0)
                    }
                }
            }
        validateInput()
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    private fun validateInput() {
        val monthDuration = _inputData.value.monthDuration
        val amount = _inputData.value.amount

        if (monthDuration !in 1..MAX_MONTH_DURATION) {
            sendAction(
                Action.ShowError(converter.getMessage(UiErrorType.INVALID_MONTH_DURATION), MONTH_DURATION),
            )
        } else if (amount !in 1..MAX_PAYMENT_AMOUNT) {
            sendAction(Action.ShowError(converter.getMessage(UiErrorType.INVALID_AMOUNT), AMOUNT))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onPaymentAddTapped() {
        sendAction(Action.SetLoading)

        val startDate = _inputData.value.startDate
        val monthDuration = _inputData.value.monthDuration
        val amount = _inputData.value.amount.toDouble()

        viewModelScope.launch {
            paymentUseCase
                .addPaymentPlanForMember(
                    memberId = memberId,
                    startDate = startDate,
                    monthDuration = monthDuration,
                    amount = amount,
                ).also {
                    when (it) {
                        is DomainResult.Error -> {
                            sendAction(Action.ShowError(converter.getMessage(it.error), AMOUNT))
                        }

                        is DomainResult.Success -> {
                            navigationService.popBack()
                        }
                    }
                }
        }
    }

    fun setMemberId(memberId: String) {
        this.memberId = memberId
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data object Ready : UiState

        data object Loading : UiState

        data class Error(
            val message: String,
            val inputType: PaymentAddScreenInputType,
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
            val message: String,
            val inputType: PaymentAddScreenInputType,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }
    }
}

private const val MAX_PAYMENT_AMOUNT = 100000
private const val MAX_MONTH_DURATION = 5

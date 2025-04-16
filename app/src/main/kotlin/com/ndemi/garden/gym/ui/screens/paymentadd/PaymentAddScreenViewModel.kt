package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType.AMOUNT
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType.MONTH_DURATION
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType.NONE
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType.START_DATE
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.entities.PaymentEntity
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.UUID

class PaymentAddScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val paymentUseCase: PaymentUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Waiting) {
    private var memberId = ""

    data class InputData(
        val startDate: DateTime = DateTime.now().withTime(0, 0, 0, 0),
        val monthDuration: Int = 0,
        val amount: Double = 0.0,
    )

    private val _inputData = MutableStateFlow(InputData())
    val inputData: StateFlow<InputData> = _inputData

    fun setData(
        startDate: DateTime = _inputData.value.startDate,
        monthDuration: String = "",
        amount: String = "",
        inputType: InputType,
    ) {
        _inputData.value =
            when (inputType) {
                NONE -> _inputData.value
                START_DATE -> _inputData.value.copy(startDate = startDate)
                MONTH_DURATION -> {
                    if (monthDuration.isNotEmpty() && monthDuration.isDigitsOnly()) {
                        _inputData.value.copy(monthDuration = monthDuration.toInt())
                    } else {
                        _inputData.value
                    }
                }
                AMOUNT -> {
                    if (amount.isNotEmpty() && amount.isDigitsOnly()) {
                        _inputData.value.copy(amount = amount.toDouble())
                    } else {
                        _inputData.value
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

        if (monthDuration < 1) {
            sendAction(
                Action.ShowError(converter.getMessage(UiError.INVALID_MONTH_DURATION), MONTH_DURATION),
            )
        } else if (amount < 1) {
            sendAction(Action.ShowError(converter.getMessage(UiError.INVALID_AMOUNT), AMOUNT))
        } else {
            sendAction(Action.SetReady)
        }
    }

    fun onPaymentAddTapped() {
        sendAction(Action.SetLoading)

        val startDate = _inputData.value.startDate
        val monthDuration = _inputData.value.monthDuration
        val amount = _inputData.value.amount

        viewModelScope.launch {
            paymentUseCase.addPaymentPlanForMember(
                PaymentEntity(
                    paymentId = memberId + UUID.randomUUID().toString(),
                    memberId = memberId,
                    startDateMillis = startDate.millis,
                    endDateMillis = startDate.plusMonths(monthDuration).millis,
                    amount = amount,
                ),
            ).also {
                when (it) {
                    is DomainResult.Error ->
                        sendAction(
                            Action.ShowError(
                                converter.getMessage(it.error),
                                AMOUNT,
                            ),
                        )

                    is DomainResult.Success -> {
                        if (startDate.plusMonths(monthDuration).isAfterNow) {
                            getMember(
                                endDate = startDate.plusMonths(monthDuration),
                                amount = amount,
                            )
                        } else {
                            navigationService.popBack()
                        }
                    }
                }
            }
        }
    }

    private fun getMember(
        endDate: DateTime,
        amount: Double,
    ) {
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(converter.getMessage(result.error), NONE))

                    is DomainResult.Success -> {
                        updateMembershipRegistration(result.data, endDate, amount)
                    }
                }
            }
        }
    }

    private fun updateMembershipRegistration(
        memberEntity: MemberEntity,
        endDate: DateTime,
        amount: Double,
    ) {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.copy(renewalFutureDateMillis = endDate.millis, amountDue = amount),
                UpdateType.MEMBERSHIP,
            ).also { result ->
                when (result) {
                    is DomainResult.Error -> {
                        sendAction(Action.ShowError(converter.getMessage(result.error), NONE))
                    }

                    is DomainResult.Success ->
                        navigationService.popBack()
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

        data class Error(val message: String, val inputType: InputType) : UiState
    }

    enum class InputType {
        NONE,
        START_DATE,
        MONTH_DURATION,
        AMOUNT,
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState): UiState = UiState.Ready
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(val message: String, val inputType: InputType) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }
    }
}

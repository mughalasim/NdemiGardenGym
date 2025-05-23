package com.ndemi.garden.gym.ui.screens.payments

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.enums.DomainErrorType
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class PaymentsScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val paymentUseCase: PaymentUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private lateinit var memberId: String

    private val _canAddPayment = MutableStateFlow(false)
    val canAddPayment: StateFlow<Boolean> = _canAddPayment

    private val _selectedDate: MutableStateFlow<DateTime> = MutableStateFlow(DateTime.now())
    val selectedDate: StateFlow<DateTime> = _selectedDate

    fun setMemberId(memberId: String) {
        this.memberId = memberId
    }

    fun getPaymentsForMember() {
        _canAddPayment.value = false
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            paymentUseCase.getPaymentPlanForMember(
                year = _selectedDate.value.year,
                memberId = memberId,
            ).collect { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, converter))

                    is DomainResult.Success -> {
                        _canAddPayment.value = result.data.canAddNewPayment
                        sendAction(
                            Action.Success(
                                payments = result.data.payments,
                                totalAmount = result.data.totalAmount,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun increaseYear() {
        _selectedDate.value = _selectedDate.value.plusYears(1)
        getPaymentsForMember()
    }

    fun decreaseYear() {
        _selectedDate.value = _selectedDate.value.minusYears(1)
        getPaymentsForMember()
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deletePayment(paymentEntity: PaymentEntity) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            paymentUseCase.deletePaymentPlanForMember(paymentEntity).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(
                            Action.ShowDomainError(
                                result.error,
                                converter,
                            ),
                        )

                    is DomainResult.Success -> getPaymentsForMember()
                }
            }
        }
    }

    fun navigateToPaymentAddScreen() {
        navigationService.open(Route.PaymentAddScreen(memberId))
    }

    fun getPermissions(memberId: String) = permissionsUseCase.getPermissions(memberId)

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val payments: List<PaymentEntity>, val totalAmount: Double) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowDomainError(
            val domainErrorType: DomainErrorType,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(errorCodeConverter.getMessage(domainErrorType))
        }

        data class Success(val payments: List<PaymentEntity>, val totalAmount: Double) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(payments, totalAmount)
        }
    }
}

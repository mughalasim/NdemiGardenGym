package com.ndemi.garden.gym.ui.screens.payments

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.PaymentEntity
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class PaymentsScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val membersUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private lateinit var selectedDate: DateTime
    private lateinit var memberId: String

    private val _canAddPayment = MutableLiveData<Boolean>()
    val canAddPayment: LiveData<Boolean> = _canAddPayment

    fun getPaymentsForMember(memberId: String, selectedDate: DateTime) {
        this.selectedDate = selectedDate
        this.memberId = memberId
        _canAddPayment.value = false
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            membersUseCase.getPaymentPlanForMember(
                year = selectedDate.year,
                memberId = memberId
            ).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, errorCodeConverter))

                    is DomainResult.Success -> {
                        _canAddPayment.value = result.data.second
                        sendAction(Action.Success(result.data.first, result.data.third))
                    }
                }
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deletePayment(paymentEntity: PaymentEntity) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            membersUseCase.deletePaymentPlanForMember(paymentEntity).also { result ->
                when (result) {
                    is DomainResult.Error -> sendAction(
                        Action.ShowDomainError(
                            result.error,
                            errorCodeConverter
                        )
                    )

                    is DomainResult.Success -> getPaymentsForMember(memberId, selectedDate)
                }
            }
        }
    }

    fun navigateToPaymentAddScreen() {
        navigationService.open(Route.PaymentAddScreen(memberId))
    }


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
            val domainError: DomainError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Error(errorCodeConverter.getMessage(domainError))
        }

        data class Success(val payments: List<PaymentEntity>, val totalAmount: Double) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(payments, totalAmount)
        }
    }
}

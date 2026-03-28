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
import com.ndemi.garden.gym.ui.utils.OBSERVE_MEMBER_PAYMENT_PLAN
import com.ndemi.garden.gym.ui.utils.OBSERVE_SETTINGS
import cv.domain.DomainResult
import cv.domain.enums.DomainErrorType
import cv.domain.mappers.PaymentPresentationMapper
import cv.domain.presentationModels.PaymentPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.SettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Suppress("detekt.LongParameterList")
class PaymentsScreenViewModel(
    private val memberId: String,
    private val jobRepository: JobRepository,
    private val converter: ErrorCodeConverter,
    private val paymentUseCase: PaymentUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
    private val settingsUseCase: SettingsUseCase,
    private val numberFormatUseCase: NumberFormatUseCase,
    private val paymentPresentationMapper: PaymentPresentationMapper,
    dateProviderRepository: DateProviderRepository,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val _canAddPayment = MutableStateFlow(false)
    val canAddPayment: StateFlow<Boolean> = _canAddPayment

    private val _selectedYear: MutableStateFlow<Int> = MutableStateFlow(dateProviderRepository.getYear())
    val selectedYear: StateFlow<Int> = _selectedYear

    init {
        getPaymentsForMember()
        jobRepository.add(
            viewModelScope.launch {
                settingsUseCase.observeSettingsChanged().collect {
                    getPaymentsForMember()
                }
            },
            OBSERVE_SETTINGS + javaClass.name,
        )
    }

    fun getPaymentsForMember() {
        _canAddPayment.value = false
        sendAction(Action.SetLoading)

        jobRepository.add(
            viewModelScope.launch {
                paymentUseCase
                    .getPaymentPlanForMember(
                        memberId = memberId,
                        year = _selectedYear.value,
                    ).collect { result ->
                        when (result) {
                            is DomainResult.Error -> {
                                sendAction(Action.ShowDomainError(result.error, converter))
                            }

                            is DomainResult.Success -> {
                                _canAddPayment.value = result.data.canAddNewPayment
                                sendAction(
                                    Action.Success(
                                        payments = result.data.payments.map { paymentPresentationMapper.getModel(it) },
                                        totalAmount = numberFormatUseCase.getCurrencyFormatted(result.data.totalAmount),
                                    ),
                                )
                            }
                        }
                    }
            },
            OBSERVE_MEMBER_PAYMENT_PLAN,
        )
    }

    fun increaseYear() {
        _selectedYear.value += 1
        getPaymentsForMember()
    }

    fun decreaseYear() {
        _selectedYear.value -= 1
        getPaymentsForMember()
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deletePayment(model: PaymentPresentationModel) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            paymentUseCase.deletePaymentPlanForMember(model).also { result ->
                when (result) {
                    is DomainResult.Error -> {
                        sendAction(
                            Action.ShowDomainError(
                                result.error,
                                converter,
                            ),
                        )
                    }

                    is DomainResult.Success -> {
                        getPaymentsForMember()
                    }
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

        data class Error(
            val message: String,
        ) : UiState

        data class Success(
            val payments: List<PaymentPresentationModel>,
            val totalAmount: String,
        ) : UiState
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

        data class Success(
            val payments: List<PaymentPresentationModel>,
            val totalAmount: String,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(payments, totalAmount)
        }
    }
}

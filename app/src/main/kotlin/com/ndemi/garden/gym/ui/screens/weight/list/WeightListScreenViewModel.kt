package com.ndemi.garden.gym.ui.screens.weight.list

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.OBSERVE_MEMBER_WEIGHTS
import cv.domain.DomainResult
import cv.domain.enums.DomainErrorType
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.WeightUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeightListScreenViewModel(
    private val application: Application,
    private val jobRepository: JobRepository,
    private val weightPresentationMapper: WeightPresentationMapper,
    dateProviderRepository: DateProviderRepository,
    private val navigationService: NavigationService,
    private val weightUseCase: WeightUseCase,
    private val converter: ErrorCodeConverter,
    private val numberFormatUseCase: NumberFormatUseCase,
) : BaseViewModel<WeightListScreenViewModel.UiState, WeightListScreenViewModel.Action>(UiState.Waiting) {
    private val _selectedYear: MutableStateFlow<Int> = MutableStateFlow(dateProviderRepository.getYear())
    val selectedYear: StateFlow<Int> = _selectedYear

    private val _weightList = MutableStateFlow<List<WeightPresentationModel>>(emptyList())
    val weightList = _weightList.asStateFlow()

    private val _weightChange = MutableStateFlow("")
    val weightChange = _weightChange.asStateFlow()

    init {
        getWeightList()
    }

    private fun getWeightList() {
        jobRepository.add(
            viewModelScope.launch {
                weightUseCase.getWeightForYear(_selectedYear.value).collect { result ->
                    when (result) {
                        is DomainResult.Error -> {
                            sendAction(Action.ShowDomainError(result.error, converter))
                            _weightList.value = emptyList()
                            _weightChange.value = ""
                        }

                        is DomainResult.Success -> {
                            _weightList.value = result.data.map { weightPresentationMapper.getModel(it) }
                            if (result.data.isNotEmpty() && result.data.size > 1) {
                                val change = result.data.first().weight - result.data.last().weight
                                _weightChange.value =
                                    "${numberFormatUseCase.getWeight(change)} ${numberFormatUseCase.getWeightUnit().symbol}"
                            } else {
                                _weightChange.value = ""
                            }
                        }
                    }
                }
            },
            OBSERVE_MEMBER_WEIGHTS,
        )
    }

    fun increaseYear() {
        _selectedYear.value += 1
        getWeightList()
    }

    fun decreaseYear() {
        _selectedYear.value -= 1
        getWeightList()
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deleteWeight(weightId: String) {
        viewModelScope.launch {
            weightUseCase.deleteWeight(weightId).also { result ->
                when (result) {
                    is DomainResult.Error -> {
                        sendAction(Action.ShowDomainError(result.error, converter))
                    }

                    is DomainResult.Success -> {
                        sendAction(Action.Success(application.getString(R.string.txt_weight_deleted)))
                    }
                }
            }
        }
    }

    fun editWeight(model: WeightPresentationModel) {
        navigationService.open(Route.WeightEditScreen(weightId = model.id, weight = model.weightValue, dateMillis = model.dateMillis))
    }

    fun addWeight() {
        navigationService.open(Route.WeightEditScreen())
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Waiting : UiState

        data class Error(
            val message: String,
        ) : UiState

        data class Success(
            val message: String,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data class ShowDomainError(
            val domainErrorType: DomainErrorType,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(errorCodeConverter.getMessage(domainErrorType))
        }

        data class Success(
            val message: String = "",
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(message)
        }
    }
}

package com.ndemi.garden.gym.ui.screens.weight.edit

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.enums.DomainErrorType
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightEditPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.WeightUseCase
import cv.domain.validator.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeightEditScreenViewModel(
    weightId: String = "",
    private val weight: String = "",
    private val dateMillis: Long = 0L,
    private val application: Application,
    private val weightUseCase: WeightUseCase,
    private val converter: ErrorCodeConverter,
    private val navigationService: NavigationService,
    weightPresentationMapper: WeightPresentationMapper,
    private val dateProviderRepository: DateProviderRepository,
    private val weightValidator: Validator,
) : BaseViewModel<WeightEditScreenViewModel.UiState, WeightEditScreenViewModel.Action>(UiState.Loading) {
    private val _weightState: MutableStateFlow<WeightUiState> = MutableStateFlow(WeightUiState())
    val weightState: StateFlow<WeightUiState> = _weightState

    init {
        val date = if (dateMillis == 0L) dateProviderRepository.getDate().time else dateMillis
        val presetWeight = if (weight.isEmpty()) 0.0 else weight.toDouble()
        _weightState.value =
            weightState.value.copy(
                isEditMode = true,
                model = weightPresentationMapper.getEditModel(WeightEntity(id = weightId, weight = presetWeight, dateMillis = date)),
            )
        validate()
    }

    fun onBackTapped() {
        navigationService.popBack()
    }

    fun onWeightValueChanged(weight: String) {
        _weightState.value =
            _weightState.value.copy(
                model = _weightState.value.model.copy(formattedWeight = weight),
            )
        validate()
    }

    fun onDateSelected(date: Long) {
        _weightState.value =
            _weightState.value.copy(
                model =
                    _weightState.value.model.copy(
                        dateMillis = date,
                        formattedDate = dateProviderRepository.format(date, DateFormatType.DAY_MONTH_YEAR),
                    ),
            )
        validate()
    }

    private fun validate() {
        val isWeightError = weightValidator.isNotValid(weightState.value.model.formattedWeight)
        val isDateError = dateProviderRepository.isAfterNow(weightState.value.model.dateMillis)

        val weightError = if (isWeightError) application.getString(R.string.error_invalid_weight) else ""
        val dateError = if (isDateError) application.getString(R.string.error_invalid_future_date) else ""

        _weightState.value =
            _weightState.value.copy(
                errorWeight = weightError,
                errorDate = dateError,
                updateEnabled = weightError.isEmpty() && dateError.isEmpty(),
            )
    }

    fun onAddTapped() {
        viewModelScope.launch {
            weightUseCase.setWeight(_weightState.value.model).also {
                when (it) {
                    is DomainResult.Error -> sendAction(Action.ShowDomainError(it.error, converter))
                    is DomainResult.Success -> navigationService.popBack()
                }
            }
        }
    }

    data class WeightUiState(
        val errorDate: String = "",
        val errorWeight: String = "",
        val updateEnabled: Boolean = false,
        val isEditMode: Boolean = false,
        val model: WeightEditPresentationModel = WeightEditPresentationModel(),
    )

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

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

package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.di.WeightValidator
import com.ndemi.garden.gym.ui.utils.OBSERVE_MEMBER_WEIGHTS
import cv.domain.entities.MemberEntity
import cv.domain.entities.WeightEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.presentationModels.WeightPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.validator.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeightViewModel(
    jobRepository: JobRepository,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val numberFormatUseCase: NumberFormatUseCase,
    private val dateProviderRepository: DateProviderRepository,
    private val weightPresentationMapper: WeightPresentationMapper,
    @param:WeightValidator private val weightValidator: Validator,
) : ViewModel() {
    data class WeightState(
        val inputText: String = "",
        val errorText: String = "",
        val unit: String = "",
        val formattedWeights: List<WeightPresentationModel> = emptyList(),
    )

    private val _weightState = MutableStateFlow(WeightState())
    val weightState = _weightState.asStateFlow()

    private val _memberEntity = MutableStateFlow(MemberEntity())
    val memberEntity = _memberEntity.asStateFlow()

    init {
        jobRepository.add(
            viewModelScope.launch {
                authUseCase.observeUser().collect { result ->
                    _memberEntity.value = result
                    _weightState.value =
                        WeightState(
                            unit = numberFormatUseCase.getWeightUnit(),
                            formattedWeights = result.trackedWeights.map { weightPresentationMapper.getModel(it) },
                        )
                }
            },
            OBSERVE_MEMBER_WEIGHTS,
        )
    }

    fun onWeightValueChanged(value: String) {
        val errorText = if (weightValidator.isNotValid(value)) "Invalid weight" else ""
        _weightState.value = _weightState.value.copy(inputText = value, errorText = errorText)
    }

    fun onAddWeightTapped() {
        val recordedWeights = _memberEntity.value.trackedWeights.toMutableList()
        recordedWeights.add(
            WeightEntity(
                weight = numberFormatUseCase.setWeight(_weightState.value.inputText.toDouble()),
                dateMillis = dateProviderRepository.getDate().time,
            ),
        )
        viewModelScope.launch {
            memberUseCase.updateMember(
                _memberEntity.value.copy(trackedWeights = recordedWeights),
                MemberUpdateType.DETAILS,
            )
        }
    }

    fun onDeleteWeightTapped(weightMillis: Long) {
        val recordedWeights = memberEntity.value.trackedWeights.toMutableList()
        recordedWeights.removeIf { it.dateMillis == weightMillis }
        viewModelScope.launch {
            memberUseCase.updateMember(
                _memberEntity.value.copy(trackedWeights = recordedWeights),
                MemberUpdateType.DETAILS,
            )
        }
    }
}

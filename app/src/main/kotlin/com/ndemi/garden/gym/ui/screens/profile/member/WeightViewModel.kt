package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.di.WeightValidator
import cv.domain.entities.MemberEntity
import cv.domain.entities.WeightEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.validator.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class WeightViewModel(
    private val job: MutableList<Job>,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    @WeightValidator private val weightValidator: Validator,
) : ViewModel() {
    data class WeightState(
        val inputText: String = "",
        val errorText: String = "",
    )

    private val _weightState = MutableStateFlow(WeightState())
    val weightState = _weightState.asStateFlow()

    private val _memberEntity = MutableStateFlow(MemberEntity())
    val memberEntity = _memberEntity.asStateFlow()

    init {
        job +=
            viewModelScope.launch {
                authUseCase.observeUser().collect { result ->
                    _memberEntity.value = result
                }
            }
    }

    fun onWeightValueChanged(value: String) {
        val errorMessage = if (weightValidator.isNotValid(value)) "Invalid weight" else ""
        _weightState.value = WeightState(value, errorMessage)
    }

    fun onAddWeightTapped() {
        val recordedWeights = _memberEntity.value.trackedWeights.toMutableList()
        recordedWeights.add(
            WeightEntity(
                weight = _weightState.value.inputText,
                dateMillis = DateTime.now().millis,
            ),
        )
        viewModelScope.launch {
            memberUseCase.updateMember(
                _memberEntity.value.copy(trackedWeights = recordedWeights),
                MemberUpdateType.DETAILS,
            )
        }
    }

    fun onDeleteWeightTapped(weightEntity: WeightEntity) {
        val recordedWeights = memberEntity.value.trackedWeights.toMutableList()
        recordedWeights.remove(weightEntity)
        viewModelScope.launch {
            memberUseCase.updateMember(
                _memberEntity.value.copy(trackedWeights = recordedWeights),
                MemberUpdateType.DETAILS,
            )
        }
    }
}

package com.ndemi.garden.gym.ui.screens.weight.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.utils.OBSERVE_WEIGHT_GRAPH
import cv.domain.DomainResult
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.WeightUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeightGraphComponentViewModel(
    private val navigationService: NavigationService,
    private val weightUseCase: WeightUseCase,
    jobRepository: JobRepository,
    dateProviderRepository: DateProviderRepository,
) : ViewModel() {
    private val _weightDataList = MutableStateFlow<List<WeightData>>(emptyList())
    val weightDataList = _weightDataList.asStateFlow()

    init {
        jobRepository.add(
            viewModelScope.launch {
                weightUseCase.getWeightForYear(dateProviderRepository.getYear()).collect { response ->
                    when (response) {
                        is DomainResult.Success -> {
                            _weightDataList.value =
                                response.data
                                    .map { weight ->
                                        WeightData(
                                            date = dateProviderRepository.getDayOfYear(weight.dateMillis).toFloat(),
                                            weight = weight.weight.toFloat(),
                                        )
                                    }.sortedBy { it.date }
                        }

                        is DomainResult.Error -> {
                            _weightDataList.value = emptyList()
                        }
                    }
                }
            },
            OBSERVE_WEIGHT_GRAPH,
        )
    }

    fun onAddWeightTapped() {
        navigationService.open(Route.WeightListScreen)
    }
}

data class WeightData(
    val date: Float,
    val weight: Float,
)

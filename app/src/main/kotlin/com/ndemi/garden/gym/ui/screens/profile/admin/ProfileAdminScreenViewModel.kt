package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.OBSERVE_ADMIN
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AdminDashboardUseCase
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ProfileAdminScreenViewModel(
    private val jobRepository: JobRepository,
    private val adminDashboardUseCase: AdminDashboardUseCase,
    private val accessUseCase: AccessUseCase,
    dateProviderRepository: DateProviderRepository,
) : BaseViewModel<ProfileAdminScreenViewModel.UiState, ProfileAdminScreenViewModel.Action>(UiState.Loading) {
    private var currentDate = dateProviderRepository.getDate()

    init {
        fetchAdminDashboard()
    }

    private fun fetchAdminDashboard() {
        sendAction(Action.Loading)
        jobRepository.add(
            viewModelScope.launch {
                adminDashboardUseCase.invoke(currentDate).collect {
                    sendAction(Action.Success(it))
                }
            },
            OBSERVE_ADMIN,
        )
    }

    fun onYearPlusTapped() {
        currentDate.year += 1
        fetchAdminDashboard()
    }

    fun onYearMinusTapped() {
        currentDate.year -= 1
        fetchAdminDashboard()
    }

    fun onMonthPlusTapped() {
        currentDate.month += 1
        fetchAdminDashboard()
    }

    fun onMonthMinusTapped() {
        currentDate.month -= 1
        fetchAdminDashboard()
    }

    fun onLogOutTapped() {
        accessUseCase.logOut()
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Success(
            val model: AdminDashboardPresentationModel,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(
            val model: AdminDashboardPresentationModel,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(model = model)
        }
    }
}

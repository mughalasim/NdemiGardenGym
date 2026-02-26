package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import cv.domain.entities.AdminDashboard
import cv.domain.entities.MemberEntity
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AdminDashboardUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ProfileAdminScreenViewModel(
    private val job: MutableList<Job>,
    private val adminDashboardUseCase: AdminDashboardUseCase,
    private val accessUseCase: AccessUseCase,
    dateProviderRepository: DateProviderRepository,
) : BaseViewModel<ProfileAdminScreenViewModel.UiState, ProfileAdminScreenViewModel.Action>(UiState.Loading) {
    private val memberEntity = MutableStateFlow(MemberEntity())
    private var currentDate = dateProviderRepository.getDate()

    init {
        fetchAdminDashboard()
    }

    private fun fetchAdminDashboard() {
        sendAction(Action.Loading)
        job +=
            viewModelScope.launch {
                adminDashboardUseCase.invoke(currentDate).collect {
                    memberEntity.value = it.memberEntity
                    sendAction(Action.Success(it))
                }
            }
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
            val adminDashboard: AdminDashboard,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(
            val adminDashboard: AdminDashboard,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Success(
                    adminDashboard = adminDashboard,
                )
        }
    }
}

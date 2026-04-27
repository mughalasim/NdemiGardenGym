package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.appSnackbar.buildInfoSnackbar
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.OBSERVE_ADMIN
import com.ndemi.garden.gym.ui.utils.OBSERVE_SETTINGS
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AdminDashboardUseCase
import cv.domain.usecase.SettingsUseCase
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ProfileAdminScreenViewModel(
    private val showSnackbar: (AppSnackbarData) -> Unit,
    private val jobRepository: JobRepository,
    private val adminDashboardUseCase: AdminDashboardUseCase,
    private val navigationService: NavigationService,
    private val settingsUseCase: SettingsUseCase,
    dateProviderRepository: DateProviderRepository,
) : BaseViewModel<ProfileAdminScreenViewModel.UiState, ProfileAdminScreenViewModel.Action>(UiState.Loading) {
    private var currentDate = dateProviderRepository.getDate()

    init {
        fetchAdminDashboard()
        jobRepository.add(
            viewModelScope.launch {
                settingsUseCase.observeSettingsChanged().collect {
                    fetchAdminDashboard()
                }
            },
            OBSERVE_SETTINGS + javaClass.name,
        )
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
        showSnackbar(buildInfoSnackbar("Increased year"))
    }

    fun onYearMinusTapped() {
        currentDate.year -= 1
        fetchAdminDashboard()
        showSnackbar(buildInfoSnackbar("Decreased year"))
    }

    fun onMonthPlusTapped() {
        currentDate.month += 1
        fetchAdminDashboard()
        showSnackbar(buildInfoSnackbar("Increased month"))
    }

    fun onMonthMinusTapped() {
        currentDate.month -= 1
        fetchAdminDashboard()
        showSnackbar(buildInfoSnackbar("Decreased month"))
    }

    fun onSettingsTapped() {
        navigationService.open(Route.SettingsScreen)
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

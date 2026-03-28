package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileAdminScreen(
    viewModel: ProfileAdminScreenViewModel = koinViewModel<ProfileAdminScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingScreenWidget()
        }

        is UiState.Success -> {
            ProfileAdminScreenDetails(
                state = state.model,
                listeners =
                    ProfileAdminScreenDetailsListeners(
                        onSettingsTapped = viewModel::onSettingsTapped,
                        onYearPlusTapped = viewModel::onYearPlusTapped,
                        onYearMinusTapped = viewModel::onYearMinusTapped,
                        onMonthPlusTapped = viewModel::onMonthPlusTapped,
                        onMonthMinusTapped = viewModel::onMonthMinusTapped,
                    ),
            )
        }
    }
}

package com.ndemi.garden.gym.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    snackbarHostState: AppSnackbarHostState,
    viewModel: SettingsScreenViewModel = koinViewModel<SettingsScreenViewModel>(),
) {
    val uiStateFlow by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    when (val state = uiStateFlow) {
        is SettingsScreenViewModel.UiState.Message -> {
            snackbarHostState.Show(type = SnackbarType.SUCCESS, message = state.message)
        }

        else -> {}
    }

    SettingsDetailsScreen(
        uiState = uiState,
        dialogState = dialogState,
        listeners =
            SettingsDetailsScreenListeners(
                onBackTapped = viewModel::navigateBack,
                onLogOutTapped = viewModel::logOut,
                onShowDialog = viewModel::showDialog,
                onDialogOptionSelected = viewModel::onDialogOptionSelected,
            ),
    )
}

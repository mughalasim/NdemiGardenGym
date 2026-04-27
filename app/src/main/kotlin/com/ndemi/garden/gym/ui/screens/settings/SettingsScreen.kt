package com.ndemi.garden.gym.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

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

package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val inputData by viewModel.inputData.collectAsState()

    RegisterDetailScreen(
        uiState = uiState,
        inputData = inputData,
        hidePassword = false,
        onSetString = viewModel::setString,
        onRegisterTapped = viewModel::onRegisterTapped,
        snackbarHostState = snackbarHostState,
    )
}

package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState =
        viewModel.uiStateFlow.collectAsState(
            initial = UiState.Waiting,
        )
    val inputData = viewModel.inputData.collectAsState()

    RegisterDetailScreen(
        uiState = uiState.value,
        inputData = inputData.value,
        hidePassword = false,
        onSetString = viewModel::setString,
        onRegisterTapped = viewModel::onRegisterTapped,
        snackbarHostState = snackbarHostState,
    )
}

package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordScreenViewModel = koinViewModel<ResetPasswordScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    val inputData = viewModel.inputData.collectAsState()

    ResetPasswordDetailsScreen(
        uiState = uiState.value,
        email = inputData.value,
        setEmail = viewModel::setEmail,
        onResetPasswordTapped = viewModel::onResetPasswordTapped,
        snackbarHostState = snackbarHostState,
    )
}

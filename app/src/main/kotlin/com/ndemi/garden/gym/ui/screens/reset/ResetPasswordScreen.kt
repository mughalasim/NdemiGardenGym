package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordScreenViewModel = koinViewModel<ResetPasswordScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

    ResetPasswordDetailsScreen(
        uiState = uiState,
        email = inputData,
        setEmail = viewModel::setEmail,
        onResetPasswordTapped = viewModel::onResetPasswordTapped,
        snackbarHostState = snackbarHostState,
    )
}

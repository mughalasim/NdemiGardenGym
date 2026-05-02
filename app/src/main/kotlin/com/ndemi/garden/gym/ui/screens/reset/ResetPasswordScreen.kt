package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordScreenViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

    ResetPasswordDetailsScreen(
        uiState = uiState,
        email = inputData,
        setEmail = viewModel::setEmail,
        onResetPasswordTapped = viewModel::onResetPasswordTapped,
    )
}

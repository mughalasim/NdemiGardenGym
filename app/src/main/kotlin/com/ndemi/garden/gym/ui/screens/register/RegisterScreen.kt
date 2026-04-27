package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegisterScreen(viewModel: RegisterScreenViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

    RegisterDetailScreen(
        uiState = uiState,
        inputData = inputData,
        hidePassword = viewModel.shouldHidePassword(),
        onSetString = viewModel::setString,
        onRegisterTapped = viewModel::onRegisterTapped,
    )
}

package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.di.RegisterMember
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>(named<RegisterMember>()),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

    RegisterDetailScreen(
        uiState = uiState,
        inputData = inputData,
        hidePassword = viewModel.shouldHidePassword(),
        onSetString = viewModel::setString,
        onRegisterTapped = viewModel::onRegisterTapped,
        snackbarHostState = snackbarHostState,
    )
}

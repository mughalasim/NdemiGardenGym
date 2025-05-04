package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.InputType
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val inputData by viewModel.inputData.collectAsState()

    LoginScreenDetails(
        uiState = uiState,
        listeners =
            LoginScreenListeners(
                onValueChanged = viewModel::setString,
                onLoginTapped = viewModel::onLoginTapped,
            ),
        email = inputData.email,
        password = inputData.password,
        snackbarHostState = snackbarHostState,
    )
}

data class LoginScreenListeners(
    val onValueChanged: (String, InputType) -> Unit = { _, _ -> },
    val onLoginTapped: () -> Unit = {},
)

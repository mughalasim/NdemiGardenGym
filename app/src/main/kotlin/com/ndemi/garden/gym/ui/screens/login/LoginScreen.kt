package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.enums.LoginScreenInputType
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

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
    val onValueChanged: (String, LoginScreenInputType) -> Unit = { _, _ -> },
    val onLoginTapped: () -> Unit = {},
)

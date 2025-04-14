package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState =
        viewModel.uiStateFlow.collectAsState(
            initial = UiState.Waiting,
        )
    val inputData = viewModel.inputData.collectAsState()

    LoginScreenDetails(
        uiState = uiState.value,
        listeners = LoginScreenListeners(
            onValueChanged = viewModel::setString,
            onLoginTapped = viewModel::onLoginTapped
        ),
        email = inputData.value.email,
        password = inputData.value.password,
        snackbarHostState = snackbarHostState,
    )
}

data class LoginScreenListeners(
    val onValueChanged: (String, InputType) -> Unit = { _, _ -> },
    val onLoginTapped: () -> Unit = {}
)

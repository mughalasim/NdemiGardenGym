package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    if (uiState.value is UiState.Success) {
        viewModel.navigateLogInSuccess()
    }

    Column {
        ToolBarWidget(title = "Register")

        if (uiState.value is UiState.Error){
            WarningWidget((uiState.value as UiState.Error).message)
        }

        RegisterDetailScreen(
            uiState = uiState,
            inputData = viewModel.inputData.value,
            hidePassword = false,
            onSetString = viewModel::setString,
            onRegisterTapped = viewModel::onRegisterTapped,
        )
    }
}

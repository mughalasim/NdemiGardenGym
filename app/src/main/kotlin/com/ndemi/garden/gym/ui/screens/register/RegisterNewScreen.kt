package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterNewScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    if (uiState.value is UiState.Success){
        viewModel.navigateBack()
    }

    Column {
        ToolBarWidget(title = "Register New Member", canNavigateBack = true){
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) {
            WarningWidget((uiState.value as UiState.Error).message)
            Spacer(modifier = Modifier.padding(padding_screen_small))
        }
        RegisterDetailScreen(
            uiState = uiState,
            hidePassword = true,
            onSetString = viewModel::setString,
            onRegisterTapped = viewModel::onRegisterNewTapped,
        )
    }
}

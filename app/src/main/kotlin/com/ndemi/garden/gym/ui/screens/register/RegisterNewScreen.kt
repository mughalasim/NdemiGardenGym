package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterNewScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Waiting)
    val inputData = viewModel.inputData.collectAsState()

    if (uiState.value is UiState.Success) {
        viewModel.navigateBack()
    }

    Column {
        ToolBarWidget(
            title = stringResource(id = R.string.txt_register_new_member),
            canNavigateBack = true,
        ) {
            viewModel.navigateBack()
        }

        RegisterDetailScreen(
            uiState = uiState.value,
            inputData = inputData.value,
            hidePassword = true,
            onSetString = viewModel::setString,
            snackbarHostState = snackbarHostState,
            onRegisterTapped = viewModel::onRegisterNewTapped,
        )
    }
}

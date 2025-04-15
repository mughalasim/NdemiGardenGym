package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterNewScreen(viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>()) {
    val uiState =
        viewModel.uiStateFlow.collectAsState(
            initial = UiState.Waiting,
        )
    if (uiState.value is UiState.Success) {
        viewModel.navigateBack()
    }
    val inputData = viewModel.inputData.collectAsState()

    Column {
        ToolBarWidget(title = stringResource(id = R.string.txt_register_new_member), canNavigateBack = true) {
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) {
            WarningWidget((uiState.value as UiState.Error).message)
        }

        if (uiState.value is UiState.Loading) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .padding(padding_screen)
                        .align(Alignment.CenterHorizontally),
                strokeCap = StrokeCap.Round,
                trackColor = AppTheme.colors.primary,
            )
        } else {
            // TODO - the viewModel needs to determine what type of registration this is
            RegisterDetailScreen(
                uiState = uiState.value,
                inputData = inputData.value,
                hidePassword = true,
                onSetString = viewModel::setString,
                onRegisterTapped = viewModel::onRegisterNewTapped,
            )
        }
    }
}

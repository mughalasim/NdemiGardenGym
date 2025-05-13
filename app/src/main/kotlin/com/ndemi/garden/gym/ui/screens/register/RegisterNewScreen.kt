package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.di.CreateMember
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun RegisterNewScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>(named<CreateMember>()),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val inputData by viewModel.inputData.collectAsState()

    if (uiState is UiState.Success) {
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
            uiState = uiState,
            inputData = inputData,
            hidePassword = viewModel.shouldHidePassword(),
            onSetString = viewModel::setString,
            snackbarHostState = snackbarHostState,
            onRegisterTapped = viewModel::onRegisterNewTapped,
        )
    }
}

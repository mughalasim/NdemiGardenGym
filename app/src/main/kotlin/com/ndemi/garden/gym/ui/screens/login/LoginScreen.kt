package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextSmall
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )

    if (uiState.value is UiState.Success){
        viewModel.navigateLogInSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBarWidget(title = "Login")

        if (uiState.value is UiState.Error){
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
        }

        Column(
            modifier = Modifier.padding(horizontal = padding_screen)
        ) {
            EditTextWidget(
                hint = "Email",
                isError = (uiState.value as? UiState.Error)?.inputType == InputType.EMAIL,
                keyboardType = KeyboardType.Email
            ){
                viewModel.setEmail(it)
            }

            EditPasswordTextWidget(
                hint = "Password",
                isError = (uiState.value as? UiState.Error)?.inputType == InputType.PASSWORD
            ){
                viewModel.setPassword(it)
            }

            ButtonWidget(
                title = "Login",
                isEnabled = uiState.value is UiState.Ready,
                isLoading = uiState.value is UiState.Loading
            ) {
                viewModel.onLoginTapped()
            }

            TextSmall(
                modifier = Modifier.padding(top = padding_screen_large),
                text = "App version: "+ BuildConfig.VERSION_NAME
            )
        }
    }
}

@AppPreview
@Composable
fun LoginScreenPreview() {
    AppThemeComposable{
        LoginScreen()
    }
}

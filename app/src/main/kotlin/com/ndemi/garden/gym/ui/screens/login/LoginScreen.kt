package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.value is UiState.Error){
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
            Spacer(modifier = Modifier.padding(padding_screen))
        }

        TextLarge(text = "Login")

        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(
            hint = "Email",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.EMAIL_INVALID
        ){
            viewModel.setEmail(it)
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        EditPasswordTextWidget(
            hint = "Password",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.PASSWORD_INVALID
        ){
            viewModel.setPassword(it)
        }
        Spacer(modifier = Modifier.padding(padding_screen))
        ButtonWidget(
            title = "Login",
            isEnabled = uiState.value is UiState.Ready
        ) {
            viewModel.onLoginTapped()
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    AppThemeComposable{
        LoginScreen()
    }
}

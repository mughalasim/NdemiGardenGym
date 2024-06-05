package com.ndemi.garden.gym.ui.screens.reset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordScreenViewModel = koinViewModel<ResetPasswordScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.value is UiState.Error){
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
            Spacer(modifier = Modifier.padding(padding_screen_small))
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        TextLarge(text = "Forgot your password?")

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Email",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.EMAIL_INVALID
        ){
            viewModel.setEmail(it)
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(
            title = "Reset",
            isEnabled = uiState.value is UiState.Ready,
            isLoading = uiState.value is UiState.Loading
        ) {
            viewModel.onResetPasswordTapped()
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ResetPasswordScreenPreview() {
    AppThemeComposable{
        ResetPasswordScreen()
    }
}
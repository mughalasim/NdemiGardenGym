package com.ndemi.garden.gym.ui.screens.register

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
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InPutType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.value is UiState.Error){
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
            Spacer(modifier = Modifier.padding(padding_screen))
        }

        TextLarge(text = "Register")

        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(
            hint = "First name",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.FIRST_NAME_INVALID
        ){
            viewModel.setString(it, InPutType.FIRST_NAME )
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(
            hint = "Last Name",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.LAST_NAME_INVALID
        ){
            viewModel.setString(it, InPutType.LAST_NAME )
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(
            hint = "Email",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.EMAIL_INVALID
        ){
            viewModel.setString(it, InPutType.EMAIL )
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        EditPasswordTextWidget(
            hint = "Password",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.PASSWORD_INVALID
        ){
            viewModel.setString(it, InPutType.PASSWORD )
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        EditPasswordTextWidget(
            hint = "Confirm password",
            isError = (uiState.value as? UiState.Error)?.uiError == UiError.PASSWORD_CONFIRM_INVALID
                    || (uiState.value as? UiState.Error)?.uiError == UiError.PASSWORD_MATCH_INVALID
        ){
            viewModel.setString(it, InPutType.CONFIRM_PASSWORD )
        }

        Spacer(modifier = Modifier.padding(padding_screen))
        ButtonWidget(
            title = "Register",
            isEnabled = uiState.value is UiState.Ready
        ) {

        }
    }
}

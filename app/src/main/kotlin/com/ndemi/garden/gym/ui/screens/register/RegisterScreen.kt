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
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = koinViewModel<RegisterScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    if (uiState.value is UiState.Success){
        viewModel.navigateLogInSuccess()
    }

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
        TextLarge(text = "Register")

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "First name",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.FIRST_NAME
        ){
            viewModel.setString(it, InputType.FIRST_NAME )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Last Name",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.LAST_NAME
        ){
            viewModel.setString(it, InputType.LAST_NAME )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Email",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.EMAIL,
            keyboardType = KeyboardType.Email
        ){
            viewModel.setString(it, InputType.EMAIL )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Apartment number",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.APARTMENT_NUMBER
        ){
            viewModel.setString(it, InputType.APARTMENT_NUMBER )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditPasswordTextWidget(
            hint = "Password",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.PASSWORD
        ){
            viewModel.setString(it, InputType.PASSWORD )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditPasswordTextWidget(
            hint = "Confirm password",
            isError = (uiState.value as? UiState.Error)?.inputType == InputType.CONFIRM_PASSWORD
        ){
            viewModel.setString(it, InputType.CONFIRM_PASSWORD )
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(
            title = "Register",
            isEnabled = uiState.value is UiState.Ready,
            isLoading = uiState.value is UiState.Loading
        ) {
            viewModel.onRegisterTapped()
        }
    }
}

package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget

@Composable
fun RegisterDetailScreen(
    uiState: State<UiState>,
    hidePassword: Boolean,
    onSetString: (String, InputType) -> Unit,
    onRegisterTapped: () -> Unit,
) {
    val currentInputType = (uiState.value as? UiState.Error)?.inputType
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "First name",
            isError = currentInputType == InputType.FIRST_NAME
        ) {
            onSetString.invoke(it, InputType.FIRST_NAME)
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Last Name",
            isError = currentInputType == InputType.LAST_NAME
        ) {
            onSetString.invoke(it, InputType.LAST_NAME)
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Email",
            isError = currentInputType == InputType.EMAIL,
            keyboardType = KeyboardType.Email
        ) {
            onSetString.invoke(it, InputType.EMAIL)
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        EditTextWidget(
            hint = "Apartment number",
            isError = currentInputType == InputType.APARTMENT_NUMBER
        ) {
            onSetString.invoke(it, InputType.APARTMENT_NUMBER)
        }

        if (!hidePassword){
            Spacer(modifier = Modifier.padding(padding_screen_small))
            EditPasswordTextWidget(
                hint = "Password",
                isError = currentInputType == InputType.PASSWORD
            ) {
                onSetString.invoke(it, InputType.PASSWORD)
            }

            Spacer(modifier = Modifier.padding(padding_screen_small))
            EditPasswordTextWidget(
                hint = "Confirm password",
                isError = currentInputType == InputType.CONFIRM_PASSWORD
            ) {
                onSetString.invoke(it, InputType.CONFIRM_PASSWORD)
            }
        } else {
            onSetString.invoke("123456", InputType.PASSWORD)
            onSetString.invoke("123456", InputType.CONFIRM_PASSWORD)
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(
            title = "Register",
            isEnabled = uiState.value is UiState.Ready,
            isLoading = uiState.value is UiState.Loading
        ) {
            onRegisterTapped.invoke()
        }
    }
}

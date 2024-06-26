package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular

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
            .padding(horizontal = padding_screen)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextRegular(
            modifier = Modifier.padding(top = padding_screen),
            text = "Register with us by filling in the details below, " +
                    "Contact the Gym Coach in order to activate your account"
        )

        EditTextWidget(
            hint = "First Name",
            isError = currentInputType == InputType.FIRST_NAME
        ) {
            onSetString.invoke(it, InputType.FIRST_NAME)
        }

        EditTextWidget(
            hint = "Last Name",
            isError = currentInputType == InputType.LAST_NAME
        ) {
            onSetString.invoke(it, InputType.LAST_NAME)
        }

        EditTextWidget(
            hint = "Email",
            isError = currentInputType == InputType.EMAIL,
            keyboardType = KeyboardType.Email
        ) {
            onSetString.invoke(it, InputType.EMAIL)
        }

        EditTextWidget(
            hint = "Apartment Number",
            isError = currentInputType == InputType.APARTMENT_NUMBER
        ) {
            onSetString.invoke(it, InputType.APARTMENT_NUMBER)
        }

        if (!hidePassword){
            EditPasswordTextWidget(
                hint = "Password",
                isError = currentInputType == InputType.PASSWORD
            ) {
                onSetString.invoke(it, InputType.PASSWORD)
            }

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

        ButtonWidget(
            title = "Register",
            isEnabled = uiState.value is UiState.Ready,
            isLoading = uiState.value is UiState.Loading
        ) {
            onRegisterTapped.invoke()
        }
    }
}

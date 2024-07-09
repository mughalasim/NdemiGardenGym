package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputData
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RegisterDetailScreen(
    uiState: State<UiState>,
    inputData: InputData? = null,
    hidePassword: Boolean = false,
    onSetString: (String, InputType) -> Unit = {_,_ ->},
    onRegisterTapped: () -> Unit = {},
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
            text = stringResource(R.string.txt_register_info)
        )

        EditTextWidget(
            hint = stringResource(R.string.txt_first_name),
            textInput = inputData?.firstName.orEmpty(),
            isError = currentInputType == InputType.FIRST_NAME
        ) {
            onSetString.invoke(it, InputType.FIRST_NAME)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_last_name),
            textInput = inputData?.lastName.orEmpty(),
            isError = currentInputType == InputType.LAST_NAME
        ) {
            onSetString.invoke(it, InputType.LAST_NAME)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_email),
            textInput = inputData?.email.orEmpty(),
            isError = currentInputType == InputType.EMAIL,
            keyboardType = KeyboardType.Email
        ) {
            onSetString.invoke(it, InputType.EMAIL)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_apartment_number),
            textInput = inputData?.apartmentNumber.orEmpty(),
            isError = currentInputType == InputType.APARTMENT_NUMBER
        ) {
            onSetString.invoke(it, InputType.APARTMENT_NUMBER)
        }

        if (!hidePassword){
            EditPasswordTextWidget(
                hint = stringResource(id = R.string.txt_password),
                textInput = inputData?.password.orEmpty(),
                isError = currentInputType == InputType.PASSWORD
            ) {
                onSetString.invoke(it, InputType.PASSWORD)
            }

            EditPasswordTextWidget(
                hint = stringResource(R.string.txt_confirm_password),
                textInput = inputData?.confirmPassword.orEmpty(),
                isError = currentInputType == InputType.CONFIRM_PASSWORD
            ) {
                onSetString.invoke(it, InputType.CONFIRM_PASSWORD)
            }
        } else {
            onSetString.invoke("123456", InputType.PASSWORD)
            onSetString.invoke("123456", InputType.CONFIRM_PASSWORD)
        }

        ButtonWidget(
            title = stringResource(R.string.txt_register),
            isEnabled = uiState.value is UiState.Ready,
            isLoading = uiState.value is UiState.Loading
        ) {
            onRegisterTapped.invoke()
        }
    }
}

@AppPreview
@Composable
fun RegisterDetailScreenPreview(){
    AppThemeComposable {
        RegisterDetailScreen(
            uiState = MutableStateFlow(UiState.Ready)
                .collectAsState(initial = UiState.Ready),
        )
    }
}

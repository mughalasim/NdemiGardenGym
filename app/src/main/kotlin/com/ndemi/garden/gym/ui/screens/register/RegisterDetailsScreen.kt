package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.RegisterScreenInputType
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputData
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.image_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun RegisterDetailScreen(
    uiState: UiState,
    inputData: InputData = InputData(),
    hidePassword: Boolean = false,
    onSetString: (String, RegisterScreenInputType) -> Unit = { _, _ -> },
    onRegisterTapped: () -> Unit = {},
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    var errorFirstName = ""
    var errorLastName = ""
    var errorEmail = ""
    var errorApartmentNumber = ""
    var errorPassword = ""
    var errorConfirmPassword = ""

    if (uiState is UiState.Error) {
        when (uiState.inputType) {
            RegisterScreenInputType.FIRST_NAME -> errorFirstName = uiState.message
            RegisterScreenInputType.LAST_NAME -> errorLastName = uiState.message
            RegisterScreenInputType.EMAIL -> errorEmail = uiState.message
            RegisterScreenInputType.APARTMENT_NUMBER -> errorApartmentNumber = uiState.message
            RegisterScreenInputType.PASSWORD -> errorPassword = uiState.message
            RegisterScreenInputType.CONFIRM_PASSWORD -> errorConfirmPassword = uiState.message
            RegisterScreenInputType.NONE ->
                snackbarHostState.Show(
                    type = SnackbarType.ERROR,
                    message = uiState.message,
                )
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .requiredWidth(page_width)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!hidePassword) {
            Image(
                modifier =
                    Modifier
                        .size(image_size_large),
                imageVector = ImageVector.vectorResource(R.drawable.ic_app),
                contentDescription = "",
            )

            TextWidget(
                style = AppTheme.textStyles.large,
                text = stringResource(R.string.txt_register),
            )

            TextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.txt_register_info),
            )
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen_large),
            hint = stringResource(R.string.txt_first_name),
            textInput = inputData.firstName,
            errorText = errorFirstName,
        ) {
            onSetString.invoke(it, RegisterScreenInputType.FIRST_NAME)
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_last_name),
            textInput = inputData.lastName,
            errorText = errorLastName,
        ) {
            onSetString.invoke(it, RegisterScreenInputType.LAST_NAME)
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_email),
            textInput = inputData.email,
            errorText = errorEmail,
            keyboardType = KeyboardType.Email,
        ) {
            onSetString.invoke(it, RegisterScreenInputType.EMAIL)
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_apartment_number),
            textInput = inputData.apartmentNumber,
            errorText = errorApartmentNumber,
        ) {
            onSetString.invoke(it, RegisterScreenInputType.APARTMENT_NUMBER)
        }

        if (!hidePassword) {
            EditTextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen),
                hint = stringResource(id = R.string.txt_password),
                textInput = inputData.password,
                errorText = errorPassword,
                isPasswordEditText = true,
            ) {
                onSetString.invoke(it, RegisterScreenInputType.PASSWORD)
            }

            EditTextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen),
                hint = stringResource(R.string.txt_confirm_password),
                textInput = inputData.confirmPassword,
                errorText = errorConfirmPassword,
                isPasswordEditText = true,
            ) {
                onSetString.invoke(it, RegisterScreenInputType.CONFIRM_PASSWORD)
            }
        }

        ButtonWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = padding_screen_large),
            title = stringResource(R.string.txt_register),
            isEnabled = uiState is UiState.Ready,
            isLoading = uiState is UiState.Loading,
            hideKeyboardOnClick = true,
        ) {
            onRegisterTapped.invoke()
        }
    }
}

@AppPreview
@Composable
private fun RegisterDetailScreenPreview() =
    AppThemeComposable {
        RegisterDetailScreen(
            uiState = UiState.Ready,
        )
    }

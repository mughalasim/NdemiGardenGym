package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputData
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun RegisterDetailScreen(
    uiState: UiState,
    inputData: InputData = InputData(),
    hidePassword: Boolean = false,
    onSetString: (String, InputType) -> Unit = { _, _ -> },
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
            InputType.FIRST_NAME -> errorFirstName = uiState.message
            InputType.LAST_NAME -> errorLastName = uiState.message
            InputType.EMAIL -> errorEmail = uiState.message
            InputType.APARTMENT_NUMBER -> errorApartmentNumber = uiState.message
            InputType.PASSWORD -> errorPassword = uiState.message
            InputType.CONFIRM_PASSWORD -> errorConfirmPassword = uiState.message
            InputType.NONE ->
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
        Image(
            modifier =
                Modifier
                    .size(icon_image_size_profile),
            imageVector = ImageVector.vectorResource(R.drawable.ic_app),
            contentDescription = "",
        )

        TextWidget(
            modifier =
                Modifier
                    .padding(horizontal = padding_screen),
            style = AppTheme.textStyles.large,
            text = stringResource(R.string.txt_register),
        )

        TextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen)
                    .padding(horizontal = padding_screen),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.txt_register_info),
        )

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_first_name),
            textInput = inputData.firstName,
            errorText = errorFirstName,
        ) {
            onSetString.invoke(it, InputType.FIRST_NAME)
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_last_name),
            textInput = inputData.lastName,
            errorText = errorLastName,
        ) {
            onSetString.invoke(it, InputType.LAST_NAME)
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
            onSetString.invoke(it, InputType.EMAIL)
        }

        EditTextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen),
            hint = stringResource(R.string.txt_apartment_number),
            textInput = inputData.apartmentNumber,
            errorText = errorApartmentNumber,
        ) {
            onSetString.invoke(it, InputType.APARTMENT_NUMBER)
        }

        if (!hidePassword) {
            EditPasswordTextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen),
                hint = stringResource(id = R.string.txt_password),
                textInput = inputData.password,
                errorText = errorPassword,
            ) {
                onSetString.invoke(it, InputType.PASSWORD)
            }

            EditPasswordTextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen),
                hint = stringResource(R.string.txt_confirm_password),
                textInput = inputData.confirmPassword,
                errorText = errorConfirmPassword,
            ) {
                onSetString.invoke(it, InputType.CONFIRM_PASSWORD)
            }
        } else {
            onSetString.invoke("123456", InputType.PASSWORD)
            onSetString.invoke("123456", InputType.CONFIRM_PASSWORD)
        }

        ButtonWidget(
            modifier =
                Modifier
                    .padding(vertical = padding_screen)
                    .padding(horizontal = padding_screen),
            title = stringResource(R.string.txt_register),
            isEnabled = uiState is UiState.Ready,
            isLoading = uiState is UiState.Loading,
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

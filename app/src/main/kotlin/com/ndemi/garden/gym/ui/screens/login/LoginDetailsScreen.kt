package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun LoginScreenDetails(
    uiState: UiState,
    listeners: LoginScreenListeners = LoginScreenListeners(),
    email: String = "",
    password: String = "",
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        var emailError = ""
        var passwordError = ""

        if (uiState is UiState.Error) {
            when (uiState.inputType) {
                InputType.NONE ->
                    snackbarHostState.Show(
                        type = SnackbarType.ERROR,
                        message = uiState.message,
                    )

                InputType.EMAIL -> emailError = uiState.message

                InputType.PASSWORD -> passwordError = uiState.message
            }
        }

        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .requiredWidth(page_width)
                    .align(Alignment.TopCenter),
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
                text = stringResource(R.string.app_name),
            )

            TextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen_large)
                        .padding(horizontal = padding_screen),
                style = AppTheme.textStyles.regular,
                text = stringResource(R.string.txt_login_desc),
            )

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                hint = stringResource(R.string.txt_email),
                textInput = email,
                errorText = emailError,
                keyboardType = KeyboardType.Email,
            ) {
                listeners.onValueChanged(it, InputType.EMAIL)
            }

            EditPasswordTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                hint = stringResource(R.string.txt_password),
                textInput = password,
                errorText = passwordError,
            ) {
                listeners.onValueChanged(it, InputType.PASSWORD)
            }

            ButtonWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen_large)
                        .padding(horizontal = padding_screen),
                title = stringResource(R.string.txt_login),
                isEnabled = uiState is UiState.Ready,
                isLoading = uiState is UiState.Loading,
                hideKeyboardOnClick = true,
                onButtonClicked = listeners.onLoginTapped,
            )

            TextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen_large)
                        .padding(horizontal = padding_screen),
                text = stringResource(R.string.txt_app_version) + BuildConfig.VERSION_NAME,
                style = AppTheme.textStyles.small,
            )
        }
    }
}

@AppPreview
@Composable
private fun LoginDetailsScreenPreview() =
    AppThemeComposable {
        LoginScreenDetails(
            uiState =
                UiState.Error(
                    message = "Invalid email address",
                    InputType.EMAIL,
                ),
        )
    }

package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.LoginScreenInputType
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.image_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.enums.MemberType

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
                LoginScreenInputType.NONE ->
                    snackbarHostState.Show(
                        type = SnackbarType.ERROR,
                        message = uiState.message,
                    )

                LoginScreenInputType.EMAIL -> emailError = uiState.message

                LoginScreenInputType.PASSWORD -> passwordError = uiState.message
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
                        .size(image_size_large),
                imageVector = ImageVector.vectorResource(R.drawable.ic_app),
                contentDescription = "",
            )

            TextWidget(
                style = AppTheme.textStyles.large,
                text = stringResource(R.string.app_name),
            )

            TextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen_large),
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
                listeners.onValueChanged(it, LoginScreenInputType.EMAIL)
            }

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                hint = stringResource(R.string.txt_password),
                textInput = password,
                errorText = passwordError,
                isPasswordEditText = true,
            ) {
                listeners.onValueChanged(it, LoginScreenInputType.PASSWORD)
            }

            ButtonWidget(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen_large),
                title = stringResource(R.string.txt_login),
                isEnabled = uiState is UiState.Ready,
                isLoading = uiState is UiState.Loading,
                hideKeyboardOnClick = true,
                onButtonClicked = listeners.onLoginTapped,
            )

            TextWidget(
                modifier =
                    Modifier
                        .padding(vertical = padding_screen_large)
                        .padding(horizontal = padding_screen),
                text = stringResource(R.string.txt_app_version) + BuildConfig.VERSION_NAME,
                style = AppTheme.textStyles.small,
            )

            if (BuildConfig.DEBUG) {
                for (member in MemberType.entries) {
                    TextWidget(
                        modifier =
                            Modifier
                                .padding(top = padding_screen_small)
                                .toAppCardStyle()
                                .clickable { listeners.onAutoCompleteTapped(member) },
                        text = member.name,
                    )
                }
            }
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
                    LoginScreenInputType.EMAIL,
                ),
        )
    }

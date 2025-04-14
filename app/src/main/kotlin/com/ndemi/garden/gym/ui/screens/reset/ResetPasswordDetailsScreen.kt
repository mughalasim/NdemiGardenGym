package com.ndemi.garden.gym.ui.screens.reset

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
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget


@Composable
fun ResetPasswordDetailsScreen(
    uiState: UiState,
    email: String = "",
    setEmail: (String) -> Unit = {},
    onResetPasswordTapped: () -> Unit = {},
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    var errorEmail = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState is UiState.Success) {
            snackbarHostState.Show(
                type = SnackbarType.SUCCESS,
                message = stringResource(R.string.txt_email_has_been_sent, uiState.email),
            )
        }

        if (uiState is UiState.Error) {
            when (uiState.inputType) {
                InputType.NONE -> snackbarHostState.Show(
                    type = SnackbarType.ERROR,
                    message = uiState.message,
                )

                InputType.EMAIL -> errorEmail = uiState.message
            }
        }

        Column(
            modifier = Modifier
                .requiredWidth(page_width),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(icon_image_size_profile),
                imageVector = ImageVector.vectorResource(R.drawable.ic_app),
                contentDescription = ""
            )

            TextWidget(
                modifier = Modifier
                    .padding(horizontal = padding_screen),
                style = AppTheme.textStyles.large,
                text = stringResource(R.string.txt_password_reset),
            )

            TextWidget(
                modifier = Modifier
                    .padding(horizontal = padding_screen)
                    .padding(top = padding_screen_large),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.txt_reset_desc)
            )

            EditTextWidget(
                modifier = Modifier
                    .padding(top = padding_screen),
                textInput = email,
                hint = stringResource(id = R.string.txt_email),
                errorText = errorEmail,
                keyboardType = KeyboardType.Email,
                onValueChanged = setEmail
            )

            ButtonWidget(
                modifier = Modifier
                    .padding(top = padding_screen_large)
                    .padding(horizontal = padding_screen),
                title = stringResource(R.string.txt_reset),
                isEnabled = uiState is UiState.Ready,
                isLoading = uiState is UiState.Loading,
                hideKeyboardOnClick = true,
                onButtonClicked = onResetPasswordTapped
            )
        }
    }
}

@AppPreview
@Composable
private fun ResetPasswordDetailsScreenPreview() =
    AppThemeComposable {
        ResetPasswordDetailsScreen(uiState = UiState.Waiting)
    }

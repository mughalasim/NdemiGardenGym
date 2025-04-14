package com.ndemi.garden.gym.ui.screens.reset

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordScreenViewModel = koinViewModel<ResetPasswordScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )
    val inputData = viewModel.inputData.collectAsState()

    ResetPasswordDetailsScreen(
        uiState = uiState.value,
        email = inputData.value,
        setEmail = viewModel::setEmail,
        onResetPasswordTapped = viewModel::onResetPasswordTapped
    )
}

@Composable
internal fun ResetPasswordDetailsScreen(
    uiState: UiState,
    email: String = "",
    setEmail: (String) -> Unit = {},
    onResetPasswordTapped: () -> Unit = {},
){
    var errorEmail = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState is UiState.Success){
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.txt_email_has_been_sent, uiState.email),
                Toast.LENGTH_LONG
            ).show()
        }

        ToolBarWidget(title = stringResource(R.string.txt_password_reset))

        if (uiState is UiState.Error){
            val message = uiState.message
            WarningWidget(message)

            if (uiState.inputType == InputType.EMAIL){
                errorEmail = uiState.message
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = padding_screen)
        ) {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen),
                text = stringResource(R.string.txt_reset_desc)
            )
            EditTextWidget(
                textInput = email,
                hint = stringResource(id = R.string.txt_email),
                errorText = errorEmail,
                keyboardType = KeyboardType.Email,
                onValueChanged = setEmail
            )

            ButtonWidget(
                title = stringResource(R.string.txt_reset),
                isEnabled = uiState is UiState.Ready,
                isLoading = uiState is UiState.Loading,
                onButtonClicked = onResetPasswordTapped
            )
        }
    }
}

@AppPreview
@Composable
private fun ResetPasswordScreenPreview() {
    AppThemeComposable{
        ResetPasswordDetailsScreen(uiState = UiState.Waiting)
    }
}

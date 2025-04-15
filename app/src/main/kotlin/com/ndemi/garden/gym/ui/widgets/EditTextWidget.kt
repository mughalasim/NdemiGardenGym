package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun EditTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "Hint",
    errorText: String = "",
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            modifier =
                modifier
                    .fillMaxWidth(),
            value = textInput,
            enabled = isEnabled,
            isError = errorText.isNotEmpty(),
            singleLine = true,
            onValueChange = {
                onValueChanged(it)
            },
            textStyle = AppTheme.textStyles.regular,
            trailingIcon = {
                if (textInput.isNotEmpty()) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = hint,
                        modifier =
                            Modifier.clickable {
                                onValueChanged("")
                            },
                        tint = if (errorText.isNotEmpty()) AppTheme.colors.error else AppTheme.colors.border,
                    )
                }
            },
            label = { Text(text = hint, style = AppTheme.textStyles.regular) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            keyboardActions =
                KeyboardActions {
                    onValueChanged.invoke(textInput)
                    keyboardController?.hide()
                },
            colors = getAppTextColors(),
        )
        EditTextWidgetBottom(errorText)
    }
}

@Composable
fun EditPasswordTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "Password",
    errorText: String = "",
    isEnabled: Boolean = true,
    onValueChanged: (String) -> Unit = {},
) {
    var passwordVisible: Boolean by rememberSaveable { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            modifier =
                modifier
                    .fillMaxWidth(),
            value = textInput,
            onValueChange = {
                onValueChanged(it)
            },
            textStyle = AppTheme.textStyles.regular,
            label = { Text(text = hint, style = AppTheme.textStyles.regular) },
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (passwordVisible) {
                        Icons.Outlined.Lock
                    } else {
                        Icons.Filled.Lock
                    }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = hint,
                        tint = if (errorText.isNotEmpty()) AppTheme.colors.error else AppTheme.colors.border,
                    )
                }
            },
            isError = errorText.isNotEmpty(),
            enabled = isEnabled,
            colors = getAppTextColors(),
        )
        EditTextWidgetBottom(errorText)
    }
}

@Composable
private fun EditTextWidgetBottom(errorText: String) {
    Spacer(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(line_thickness)
                .padding(horizontal = padding_screen)
                .background(if (errorText.isNotEmpty()) AppTheme.colors.error else AppTheme.colors.border),
    )
    TextWidget(
        modifier =
            Modifier
                .padding(horizontal = padding_screen)
                .padding(top = padding_screen_tiny),
        style = AppTheme.textStyles.small,
        color = AppTheme.colors.error,
        text = errorText,
    )
}

@Composable
private fun getAppTextColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = AppTheme.colors.textPrimary,
        focusedTrailingIconColor = AppTheme.colors.primary,
        focusedBorderColor = Color.Transparent,
        focusedLabelColor = AppTheme.colors.primary,
        unfocusedTextColor = AppTheme.colors.textPrimary,
        unfocusedTrailingIconColor = AppTheme.colors.backgroundButtonDisabled,
        unfocusedBorderColor = Color.Transparent,
        unfocusedLabelColor = AppTheme.colors.primary,
        disabledTextColor = AppTheme.colors.textSecondary,
        disabledTrailingIconColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        disabledLabelColor = AppTheme.colors.textSecondary,
        errorTextColor = AppTheme.colors.error,
        errorTrailingIconColor = AppTheme.colors.error,
        errorBorderColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    )

@AppPreview
@Composable
private fun EditTextWidgetPreview() {
    AppThemeComposable {
        Column {
            EditTextWidget()
            EditTextWidget(textInput = "Normal")
            EditTextWidget(
                textInput = "Error",
                errorText = "Some error is shown here",
            )
            EditTextWidget(textInput = "Disabled", isEnabled = false)
        }
    }
}

@AppPreview
@Composable
private fun EditPasswordTextWidgetPreview() {
    AppThemeComposable {
        Column {
            EditPasswordTextWidget()
            EditPasswordTextWidget(textInput = "Normal")
            EditPasswordTextWidget(textInput = "Error", errorText = "Some error is here")
            EditPasswordTextWidget(textInput = "Disabled", isEnabled = false)
        }
    }
}

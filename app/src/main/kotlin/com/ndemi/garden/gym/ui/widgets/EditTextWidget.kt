package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun EditTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "",
    errorText: String = "",
    isEnabled: Boolean = true,
    canClear: Boolean = true,
    isPasswordEditText: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textColor = if (errorText.isEmpty()) AppTheme.colors.textPrimary else AppTheme.colors.error
    var passwordVisible: Boolean by rememberSaveable { mutableStateOf(false) }
    val trailingIcon =
        if (!isPasswordEditText) {
            Icons.Default.Clear
        } else if (passwordVisible) {
            Icons.Outlined.Lock
        } else {
            Icons.Filled.Lock
        }

    Column(modifier = modifier) {
        TextWidget(
            text = hint,
            style = AppTheme.textStyles.small,
            color = AppTheme.colors.textSecondary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(vertical = padding_screen_small),
                value = textInput,
                onValueChange = onValueChanged,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Done),
                enabled = isEnabled,
                singleLine = true,
                keyboardActions =
                    KeyboardActions(
                        onDone = { keyboardController?.hide() },
                    ),
                visualTransformation =
                    if (!isPasswordEditText || passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                textStyle = AppTheme.textStyles.regular.copy(color = textColor),
                cursorBrush = SolidColor(AppTheme.colors.primary),
            )
            if (textInput.isNotEmpty() && isEnabled && canClear) {
                Icon(
                    trailingIcon,
                    contentDescription = hint,
                    modifier =
                        Modifier
                            .padding(horizontal = padding_screen_tiny)
                            .clickable {
                                if (isPasswordEditText) {
                                    passwordVisible = !passwordVisible
                                } else {
                                    onValueChanged("")
                                }
                            },
                    tint = if (errorText.isNotEmpty()) AppTheme.colors.error else AppTheme.colors.border,
                )
            }
        }
        EditTextBottomComponent(errorText)
    }
}

@Composable
private fun EditTextBottomComponent(errorText: String) {
    Spacer(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(line_thickness)
                .background(if (errorText.isNotEmpty()) AppTheme.colors.error else AppTheme.colors.border),
    )
    TextWidget(
        modifier =
            Modifier
                .padding(top = padding_screen_tiny),
        style = AppTheme.textStyles.small,
        color = AppTheme.colors.error,
        text = errorText,
    )
}

@AppPreview
@Composable
private fun EditTextWidgetPreview() {
    AppThemeComposable {
        Column {
            EditTextWidget(
                textInput = "Lorem Ipsum text",
                errorText = "Error message",
            )

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                textInput = "Some test",
                canClear = false,
            )

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                textInput = "Some test",
                isPasswordEditText = true,
            )

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                textInput = "Some test",
                hint = "Hint text",
                isEnabled = false,
            )
        }
    }
}

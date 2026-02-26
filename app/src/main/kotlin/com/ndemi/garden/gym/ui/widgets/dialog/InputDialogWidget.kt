package com.ndemi.garden.gym.ui.widgets.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun InputDialogWidget(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    editTextInput: String = "",
    editTextError: String = "",
    editTextKeyboardType: KeyboardType = KeyboardType.Text,
    editTextValueChanged: (String) -> Unit = {},
    isDismissed: Boolean = true,
    onDismissed: () -> Unit = {},
    positiveButton: String = stringResource(R.string.txt_ok),
    positiveOnClick: () -> Unit = {},
    negativeButton: String = "",
    negativeOnClick: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        containerColor = AppTheme.colors.backgroundCard,
        tonalElevation = 15.dp,
        shape = RoundedCornerShape(border_radius),
        title = {
            TextWidget(
                text = title,
                style = AppTheme.textStyles.large,
            )
        },
        text = {
            Column {
                TextWidget(text = message)
                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    textInput = editTextInput,
                    onValueChanged = editTextValueChanged,
                    errorText = editTextError,
                    keyboardType = editTextKeyboardType,
                )
            }
        },
        onDismissRequest = onDismissed,
        confirmButton = {
            ButtonWidget(
                modifier = Modifier.padding(start = padding_screen),
                title = positiveButton,
                onButtonClicked = positiveOnClick,
                isEnabled = editTextError.isEmpty(),
            )
        },
        dismissButton = {
            if (negativeButton.isNotBlank()) {
                ButtonWidget(
                    title = negativeButton,
                    onButtonClicked = negativeOnClick,
                    isOutlined = true,
                )
            }
        },
        properties =
            DialogProperties(
                dismissOnClickOutside = isDismissed,
                dismissOnBackPress = isDismissed,
            ),
    )
}

@AppPreview
@Composable
private fun InputDialogWidgetPreview() =
    AppThemeComposable {
        InputDialogWidget(
            title = "Weight Capture",
            message = "Enter your weight in the field provided",
            positiveButton = "Update",
            negativeButton = "Cancel",
            editTextInput = "",
            editTextError = "cannot be empty",
        )
    }

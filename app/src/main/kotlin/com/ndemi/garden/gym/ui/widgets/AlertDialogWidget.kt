package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun AlertDialogWidget(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
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
            TextWidget(text = message)
        },
        onDismissRequest = onDismissed,
        confirmButton = {
            ButtonWidget(
                modifier = Modifier.padding(start = padding_screen),
                title = positiveButton,
                onButtonClicked = positiveOnClick,
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
private fun AlertDialogWidgetPreview() =
    AppThemeComposable {
        AlertDialogWidget(
            title = "Some title",
            message = "Some long message will be shown here",
            positiveButton = "Yes",
            negativeButton = "NO",
        )
    }

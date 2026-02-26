package com.ndemi.garden.gym.ui.widgets.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun AlertDialogWidget(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    listItems: List<String> = emptyList(),
    isDismissable: Boolean = true,
    onDismissed: () -> Unit = {},
    onListItemClicked: (String) -> Unit = {},
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
                for (item in listItems) {
                    ButtonWidget(
                        modifier = Modifier.fillMaxWidth().padding(top = padding_screen_small),
                        overridePadding = padding_screen_tiny,
                        title = item,
                        isOutlined = true,
                        onButtonClicked = { onListItemClicked(item) },
                    )
                }
            }
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
                dismissOnClickOutside = isDismissable,
                dismissOnBackPress = isDismissable,
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
            listItems = listOf("Option 1", "option 2"),
            positiveButton = "Yes",
            negativeButton = "NO",
        )
    }

package com.ndemi.garden.gym.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small

@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean,
    onButtonClicked: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .background(
                    color =
                        if (isEnabled) {
                            AppTheme.colors.backgroundButtonEnabled
                        } else {
                            AppTheme.colors.backgroundButtonDisabled
                        },
                    shape = RoundedCornerShape(border_radius),
                )
                .clickable {
                    onButtonClicked()
                },
    ) {
        TextSmall(
            modifier =
                Modifier
                    .padding(padding_screen_small),
            text = title.uppercase(),
            color = if (isEnabled) AppTheme.colors.backgroundScreen else AppTheme.colors.textSecondary,
        )
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ButtonWidgetPreviewNight() {
    AppThemeComposable {
        Column {
            ButtonWidget(title = "Enabled button", isEnabled = true) {}
            ButtonWidget(title = "Disabled button", isEnabled = false) {}
        }
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun ButtonWidgetPreview() {
    AppThemeComposable {
        Column {
            ButtonWidget(title = "Enabled button", isEnabled = true) {}
            ButtonWidget(title = "Disabled button", isEnabled = false) {}
        }
    }
}

package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun WarningWidget(
    message: String,
    modifier: Modifier = Modifier,
) {
    TextWidget(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Top)
                .background(color = AppTheme.colors.error)
                .padding(vertical = padding_screen_small)
                .padding(horizontal = padding_screen),
        text = message,
        color = Color.White,
        textAlign = TextAlign.Center,
    )
}

@AppPreview
@Composable
private fun WarningWidgetPreview() =
    AppThemeComposable {
        WarningWidget("Warning message will be placed here!, This message could overflow")
    }

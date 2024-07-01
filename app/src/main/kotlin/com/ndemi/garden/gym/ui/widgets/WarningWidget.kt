package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun WarningWidget(title: String) {
    TextRegular(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        AppTheme.colors.backgroundError,
                        AppTheme.colors.backgroundButtonDisabled,
                        )
                ),
            )
            .padding(padding_screen_small),
        text = title,
        color = Color.White,
    )
}

@AppPreview
@Composable
fun WarningWidgetPreview() {
    AppThemeComposable {
        WarningWidget("Warning message will be placed here!, This message could overflow")
    }
}

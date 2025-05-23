package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny

@Composable
fun StatsTextWidget(
    value: String,
    description: String,
    overrideColor: Color = AppTheme.colors.primary,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextWidget(
            color = overrideColor,
            text = value,
            style = AppTheme.textStyles.large,
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen_tiny),
            text = description,
            style = AppTheme.textStyles.small,
        )
    }
}

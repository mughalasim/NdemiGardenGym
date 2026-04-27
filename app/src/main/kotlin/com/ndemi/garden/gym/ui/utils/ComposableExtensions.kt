package com.ndemi.garden.gym.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small

@Composable
fun Modifier.toAppCardStyle(overridePadding: Dp = padding_screen_small) =
    this
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            color = AppTheme.colors.backgroundCard,
            shape = RoundedCornerShape(border_radius),
        ).padding(overridePadding)

@Composable
@Suppress("Detekt:MagicNumber")
fun getBMIColor(bmi: Double) =
    when {
        bmi > 40 -> AppTheme.colors.error
        bmi > 35 -> Color.Red
        bmi !in 18.5..25.0 -> Color.Magenta
        else -> AppTheme.colors.success
    }

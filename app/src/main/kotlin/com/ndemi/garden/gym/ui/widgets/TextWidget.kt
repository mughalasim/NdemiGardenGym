package com.ndemi.garden.gym.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppTheme

@Composable
fun TextRegular(
    modifier: Modifier = Modifier,
    text: String = "Test String",
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = AppTheme.textStyles.regular,
            color = color,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}

@Composable
fun TextRegularBold(
    modifier: Modifier = Modifier,
    text: String = "Test String",
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = AppTheme.textStyles.regularBold,
            color = color,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}

@Composable
fun TextSmall(
    modifier: Modifier = Modifier,
    text: String = "Test String",
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = AppTheme.textStyles.small,
            color = color,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}

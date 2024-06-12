package com.ndemi.garden.gym.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme

@Preview(showBackground = true)
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

@Preview(showBackground = true)
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

@Preview(showBackground = true)
@Composable
fun TextLarge(
    modifier: Modifier = Modifier,
    text: String = "Test String",
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = AppTheme.textStyles.large,
            color = color,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}

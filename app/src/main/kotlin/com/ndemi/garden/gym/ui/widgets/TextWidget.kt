package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun TextWidget(
    modifier: Modifier = Modifier,
    text: String = "",
    style: TextStyle = AppTheme.textStyles.regular,
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
    textDecoration: TextDecoration = TextDecoration.None,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = style,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}

@AppPreview
@Composable
private fun TextWidgetPreview() =
    AppThemeComposable {
        Column {
            TextWidget(text = "Testing text", style = AppTheme.textStyles.small)
            TextWidget(text = "Testing text", style = AppTheme.textStyles.regular)
            TextWidget(text = "Testing text", style = AppTheme.textStyles.regularBold)
        }
    }

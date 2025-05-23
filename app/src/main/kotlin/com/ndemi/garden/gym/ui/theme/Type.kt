package com.ndemi.garden.gym.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val localFontFamily = FontFamily.SansSerif

data class AppTextStyles(
    val small: TextStyle =
        TextStyle(
            fontFamily = localFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = text_size_small,
        ),
    val regular: TextStyle =
        TextStyle(
            fontFamily = localFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = text_size_regular,
        ),
    val regularBold: TextStyle =
        TextStyle(
            fontFamily = localFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = text_size_regular,
        ),
    val large: TextStyle =
        TextStyle(
            fontFamily = localFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = text_size_large,
        ),
    val largeExtra: TextStyle =
        TextStyle(
            fontFamily = localFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = text_size_large_extra,
        ),
)

val LocalTextStyles = staticCompositionLocalOf { AppTextStyles() }

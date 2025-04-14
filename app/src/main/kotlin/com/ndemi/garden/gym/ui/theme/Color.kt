package com.ndemi.garden.gym.ui.theme

import androidx.compose.ui.graphics.Color

@Suppress("detekt.MagicNumber")
val orange = Color(0xFFF65E69)

@Suppress("detekt.MagicNumber")
val red = Color(0xFFC90C0C)

@Suppress("detekt.MagicNumber")
val green = Color(0xFF368139)

@Suppress("detekt.MagicNumber")
val lightBlue = Color(0xFFC0CADA)

@Suppress("detekt.MagicNumber")
val lighterBlue = Color(0xFFF4F7FD)

@Suppress("detekt.MagicNumber")
val darkBlue = Color(0xFF31078F)

@Suppress("detekt.MagicNumber")
val lightGrey = Color(0xFF8D939C)

@Suppress("detekt.MagicNumber")
val darkGrey = Color(0xFF3B3A3C)

@Suppress("detekt.MagicNumber")
val darkerGrey = Color(0xFF232324)

val DarkAppColors =
    AppColors(
        primary = orange,
        textPrimary = Color.White,
        textSecondary = lightGrey,
        backgroundScreen = Color.Black,
        backgroundCard = darkerGrey,
        border = darkGrey,
        error = red,
        success = green,
        backgroundButtonDisabled = darkGrey,
        backgroundButtonEnabled = Color.White,
    )

val LightAppColors =
    AppColors(
        primary = orange,
        textPrimary = darkerGrey,
        textSecondary = darkGrey,
        backgroundScreen = Color.White,
        backgroundCard = lighterBlue,
        border = lightGrey,
        error = red,
        success = green,
        backgroundButtonDisabled = lightBlue,
        backgroundButtonEnabled = darkBlue,
    )

@Suppress("detekt.LongParameterList")
class AppColors(
    val primary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val backgroundScreen: Color,
    val backgroundCard: Color,
    val border: Color,
    val error: Color,
    val success: Color,
    val backgroundButtonDisabled: Color,
    val backgroundButtonEnabled: Color,
)

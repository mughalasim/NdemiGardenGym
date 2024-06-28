package com.ndemi.garden.gym.ui.theme

import androidx.compose.ui.graphics.Color

@Suppress("detekt.MagicNumber")
val Red = Color(0xFFC90C0C)

@Suppress("detekt.MagicNumber")
val Yellow = Color(0xFFB69547)

@Suppress("detekt.MagicNumber")
val LightGreen = Color(0xFFA0E294)

@Suppress("detekt.MagicNumber")
val DarkBlue = Color(0xFF31078F)

@Suppress("detekt.MagicNumber")
val LightBlue = Color(0xFFCBD0D8)

@Suppress("detekt.MagicNumber")
val DarkGrey = Color(0xFF3B3A3C)

@Suppress("detekt.MagicNumber")
val LightGrey = Color(0xFF8D939C)

val DarkAppColors =
    AppColors(
        textPrimary = Color.White,
        textSecondary = LightGrey,
        backgroundTitleBar = Color.Black,
        backgroundScreen = Color.Black,
        backgroundCardBorder = LightBlue,
        backgroundError = Red,
        backgroundButtonDisabled = DarkGrey,
        backgroundButtonEnabled = Color.White,
        highLight = LightGreen,
    )

val LightAppColors =
    AppColors(
        textPrimary = DarkBlue,
        textSecondary = DarkGrey,
        backgroundTitleBar = DarkBlue,
        backgroundScreen = Color.White,
        backgroundCardBorder = LightBlue,
        backgroundError = Red,
        backgroundButtonDisabled = LightBlue,
        backgroundButtonEnabled = DarkBlue,
        highLight = Yellow,
    )

@Suppress("detekt.LongParameterList")
class AppColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val backgroundTitleBar: Color,
    val backgroundScreen: Color,
    val backgroundCardBorder: Color,
    val backgroundError: Color,
    val backgroundButtonDisabled: Color,
    val backgroundButtonEnabled: Color,
    val highLight: Color,
)

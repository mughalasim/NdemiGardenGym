package com.ndemi.garden.gym.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

object AppTheme {
    val colors: AppColors
        @Composable
        get() = getColors()

    val textStyles: AppTextStyles
        @Composable
        get() = LocalTextStyles.current
}

@Composable
fun getColors(): AppColors {
    val isDarkMode = isSystemInDarkTheme()
    return remember { if (isDarkMode) DarkAppColors else LightAppColors }
}

@Composable
fun AppThemeComposable(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getColors().backgroundScreen,
        content = content,
    )
}

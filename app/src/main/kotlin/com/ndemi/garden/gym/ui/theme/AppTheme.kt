package com.ndemi.garden.gym.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    val systemUiController = rememberSystemUiController()
    val isDarkMode = isSystemInDarkTheme()
    val currentColors = remember { if (isDarkMode) DarkAppColors else LightAppColors }

    DisposableEffect(systemUiController, isSystemInDarkTheme()) {
        systemUiController.setSystemBarsColor(
            color = currentColors.backgroundTitleBar,
        )
        systemUiController.setStatusBarColor(
            color = currentColors.backgroundTitleBar,
        )
        systemUiController.setNavigationBarColor(
            color = currentColors.backgroundTitleBar,
        )
        onDispose { }
    }

    return currentColors
}

@Composable
fun AppThemeComposable(content: @Composable () -> Unit) {
    val currentColors = getColors()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = currentColors.backgroundScreen,
        content = content,
    )
}

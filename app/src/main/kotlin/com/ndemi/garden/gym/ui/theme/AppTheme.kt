package com.ndemi.garden.gym.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
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
fun AppThemeComposable(
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = getColors().backgroundScreen,
        contentColor = getColors().backgroundScreen,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content,
    )
}

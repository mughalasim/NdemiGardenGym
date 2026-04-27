package com.ndemi.garden.gym.ui.appSnackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small

@Composable
fun AppSnackbarWidget(
    data: SnackbarData,
    type: AppSnackbarType,
) {
    val containerColor =
        when (type) {
            AppSnackbarType.SUCCESS -> {
                AppTheme.colors.success
            }

            AppSnackbarType.ERROR -> {
                AppTheme.colors.error
            }

            AppSnackbarType.INFO -> {
                AppTheme.colors.backgroundButtonDisabled
            }
        }
    Snackbar(
        modifier = Modifier.padding(padding_screen_small),
        shape = RoundedCornerShape(border_radius),
        snackbarData = data,
        containerColor = containerColor,
        contentColor = Color.White,
        actionColor = Color.White,
        actionContentColor = AppTheme.colors.textPrimary,
        dismissActionContentColor = AppTheme.colors.textPrimary,
    )
}

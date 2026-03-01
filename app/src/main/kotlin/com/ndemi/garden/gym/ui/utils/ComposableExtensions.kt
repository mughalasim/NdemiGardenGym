package com.ndemi.garden.gym.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Modifier.toAppCardStyle(overridePadding: Dp = padding_screen_small) =
    this
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            color = AppTheme.colors.backgroundCard,
            shape = RoundedCornerShape(border_radius),
        ).padding(overridePadding)

@Composable
@Suppress("Detekt:MagicNumber")
fun getBMIColor(bmi: Double) =
    when {
        bmi > 40 -> AppTheme.colors.error
        bmi > 35 -> Color.Red
        bmi !in 18.5..25.0 -> Color.Magenta
        else -> AppTheme.colors.success
    }

@Composable
fun StateFlow<BaseViewModel.SnackbarState>.ObserveAppSnackbar(snackbarHostState: AppSnackbarHostState) {
    val snackbarState by this.collectAsStateWithLifecycle()
    when (snackbarState) {
        is BaseViewModel.SnackbarState.Visible -> {
            snackbarHostState.Show(
                type = (snackbarState as BaseViewModel.SnackbarState.Visible).snackbarType,
                message = (snackbarState as BaseViewModel.SnackbarState.Visible).message,
            )
        }

        else -> {}
    }
}

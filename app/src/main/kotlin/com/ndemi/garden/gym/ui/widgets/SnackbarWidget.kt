package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small

@Composable
fun SnackbarWidget(
    modifier: Modifier = Modifier,
    data: SnackbarData,
    type: SnackbarType,
) {
    val actionColor: Color = Color.White
    val contentColor: Color = Color.White
    val actionContentColor: Color = AppTheme.colors.textPrimary
    val dismissActionContentColor: Color = AppTheme.colors.textPrimary

    val containerColor =
        when (type) {
            SnackbarType.SUCCESS -> {
                AppTheme.colors.success
            }

            SnackbarType.ERROR -> {
                AppTheme.colors.error
            }
        }

    Snackbar(
        modifier = modifier.padding(padding_screen_small),
        shape = RoundedCornerShape(border_radius),
        snackbarData = data,
        containerColor = containerColor,
        contentColor = contentColor,
        actionColor = actionColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
    )
}

data class AppSnackbarHostState(
    private var type: SnackbarType = SnackbarType.SUCCESS,
    val hostState: SnackbarHostState = SnackbarHostState(),
) {
    @Composable
    fun Show(
        type: SnackbarType,
        title: String = "",
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
    ) {
        this.type = type
        LaunchedEffect(type) {
            hostState.showSnackbar(
                message = if (title.isNotEmpty()) title.plus("\n").plus(message) else message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
            )
        }
    }

    @Composable
    fun SnackbarContent(
        modifier: Modifier = Modifier,
        snackbarData: SnackbarData,
    ) = SnackbarWidget(
        modifier = modifier,
        data = snackbarData,
        type = type,
    )
}

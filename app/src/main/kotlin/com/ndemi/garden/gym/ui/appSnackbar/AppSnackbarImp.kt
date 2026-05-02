package com.ndemi.garden.gym.ui.appSnackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

class AppSnackbarImp : AppSnackbar {
    private var type: AppSnackbarType = AppSnackbarType.SUCCESS

    @Composable
    override fun Show(
        hostState: SnackbarHostState,
        data: AppSnackbarData,
    ) {
        this.type = data.type
        LaunchedEffect(data.message) {
            hostState.showSnackbar(
                message =
                    if (data.title.isNotEmpty()) {
                        data.title.plus("\n").plus(data.message)
                    } else {
                        data.message
                    },
                actionLabel = data.actionLabel,
                withDismissAction = data.withDismissAction,
            )
        }
    }

    @Composable
    override fun SetContent(snackbarData: SnackbarData) =
        AppSnackbarWidget(
            data = snackbarData,
            type = type,
        )
}

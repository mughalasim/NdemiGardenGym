package com.ndemi.garden.gym.ui.appSnackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

interface AppSnackbar {
    @Composable
    fun Show(
        hostState: SnackbarHostState,
        data: AppSnackbarData,
    )

    @Composable
    fun SetContent(snackbarData: SnackbarData)
}

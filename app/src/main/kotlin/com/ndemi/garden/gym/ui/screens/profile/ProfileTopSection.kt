package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget

@Composable
fun ProfileTopSection(onLogoutTapped: () -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_are_you_sure),
            message = stringResource(R.string.txt_are_you_sure_log_out),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_logout),
            positiveOnClick = {
                showDialog = !showDialog
                onLogoutTapped.invoke()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = {
                showDialog = !showDialog
            },
        )
    }
    ToolBarWidget(
        title = stringResource(R.string.txt_profile),
        secondaryIcon = Icons.AutoMirrored.Filled.Logout,
        onSecondaryIconPressed = { showDialog = !showDialog },
    )
}

package com.ndemi.garden.gym.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.SettingType
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget

@Composable
fun SettingsDetailsScreen(
    uiState: SettingsScreenViewModel.SettingsUiState = SettingsScreenViewModel.SettingsUiState(),
    dialogState: SettingsScreenViewModel.SettingsDialogState = SettingsScreenViewModel.SettingsDialogState(),
    listeners: SettingsDetailsScreenListeners = SettingsDetailsScreenListeners(),
) {
    var showLogOutDialog by remember { mutableStateOf(false) }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_settings),
            canNavigateBack = true,
            onBackPressed = listeners.onBackTapped,
            secondaryIcon = Icons.AutoMirrored.Filled.Logout,
            onSecondaryIconPressed = {
                showLogOutDialog = !showLogOutDialog
            },
        )

        TextWidget(
            modifier =
                Modifier
                    .padding(horizontal = padding_screen)
                    .padding(top = padding_screen),
            text = stringResource(R.string.txt_settings_desc),
        )

        SettingComponent(
            description = stringResource(R.string.txt_settings_weight),
            currentSetting = uiState.weightSetting,
            icon = Icons.Default.Scale,
            onClick = {
                listeners.onShowDialog.invoke(SettingType.WEIGHT)
            },
        )

        SettingComponent(
            description = stringResource(R.string.txt_settings_height),
            currentSetting = uiState.heightSetting,
            icon = Icons.Default.Height,
            onClick = {
                listeners.onShowDialog.invoke(SettingType.HEIGHT)
            },
        )

        SettingComponent(
            description = stringResource(R.string.txt_settings_currency),
            currentSetting = uiState.currencySetting,
            icon = Icons.Default.AttachMoney,
            onClick = {
                listeners.onShowDialog.invoke(SettingType.CURRENCY)
            },
        )
    }

    if (dialogState.showDialog) {
        AlertDialogWidget(
            title = dialogState.title,
            message = dialogState.message,
            onDismissed = { listeners.onDialogOptionSelected("", dialogState.settingType) },
            listItems = dialogState.listItems,
            positiveButton = stringResource(R.string.txt_cancel),
            positiveOnClick = {
                listeners.onDialogOptionSelected("", dialogState.settingType)
            },
            onListItemClicked = {
                listeners.onDialogOptionSelected(it, dialogState.settingType)
            },
        )
    }

    if (showLogOutDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_are_you_sure),
            message = stringResource(R.string.txt_are_you_sure_log_out),
            onDismissed = { showLogOutDialog = !showLogOutDialog },
            positiveButton = stringResource(R.string.txt_logout),
            positiveOnClick = {
                showLogOutDialog = !showLogOutDialog
                listeners.onLogOutTapped.invoke()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = {
                showLogOutDialog = !showLogOutDialog
            },
        )
    }
}

@Composable
private fun SettingComponent(
    description: String = "",
    currentSetting: String = "",
    icon: ImageVector = Icons.Default.Settings,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(enabled = true, onClick = { onClick() })
                .padding(horizontal = padding_screen)
                .padding(top = padding_screen),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier = Modifier.size(icon_size_small),
            imageVector = icon,
            contentDescription = description,
            tint = AppTheme.colors.primary,
        )
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = padding_screen_small),
        ) {
            TextWidget(
                text = description,
            )
            TextWidget(
                style = AppTheme.textStyles.small,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(top = padding_screen_tiny),
                text = currentSetting,
            )
        }

        Icon(
            modifier = Modifier.size(icon_size_small),
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = description,
            tint = AppTheme.colors.primary,
        )
    }
}

data class SettingsDetailsScreenListeners(
    val onBackTapped: () -> Unit = {},
    val onLogOutTapped: () -> Unit = {},
    val onShowDialog: (SettingType) -> Unit = {},
    val onDialogOptionSelected: (String, SettingType) -> Unit = { _, _ -> },
)

@AppPreview
@Composable
private fun SettingsDetailsScreenPreview() =
    AppThemeComposable {
        SettingsDetailsScreen()
    }

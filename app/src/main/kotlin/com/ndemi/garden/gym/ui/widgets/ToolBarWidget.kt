package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun ToolBarWidget(
    title: String,
    canNavigateBack: Boolean = false,
    secondaryIcon: ImageVector? = null,
    onSecondaryIconPressed: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val backgroundColor = AppTheme.colors.backgroundButtonDisabled
    Row(
        modifier =
            Modifier
                .shadow(elevation = padding_screen_tiny)
                .background(backgroundColor)
                .padding(padding_screen)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(
            modifier =
                Modifier
                    .clickable(
                        enabled = canNavigateBack,
                    ) { onBackPressed.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint =
                if (canNavigateBack) {
                    AppTheme.colors.textPrimary
                } else {
                    backgroundColor
                },
            contentDescription = null,
        )
        TextWidget(
            modifier = Modifier.weight(1f),
            text = title,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
            style = AppTheme.textStyles.regularBold,
        )
        Icon(
            modifier =
                Modifier
                    .clickable(
                        enabled = secondaryIcon != null,
                    ) { onSecondaryIconPressed.invoke() },
            imageVector = secondaryIcon ?: Icons.Default.Settings,
            tint =
                if (secondaryIcon != null) {
                    AppTheme.colors.textPrimary
                } else {
                    backgroundColor
                },
            contentDescription = null,
        )
    }
}

@AppPreview
@Composable
private fun ToolBarWidgetPreview() {
    AppThemeComposable {
        Column {
            ToolBarWidget(
                title = "Test Toolbar",
                canNavigateBack = true,
            )
            ToolBarWidget(
                title = "Test Toolbar",
            )
            ToolBarWidget(
                title = "Extra button",
                canNavigateBack = true,
                secondaryIcon = Icons.Default.Delete,
                onSecondaryIconPressed = {},
            )
        }
    }
}

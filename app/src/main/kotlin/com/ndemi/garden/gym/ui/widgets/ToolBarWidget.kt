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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun ToolBarWidget(
    title: String,
    canNavigateBack: Boolean = false,
    onBackPressed: () -> Unit = {},
) {
    Row(
        modifier =
        Modifier
            .background(color = AppTheme.colors.highLight)
            .padding(padding_screen)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (canNavigateBack){
            Icon(
                modifier = Modifier
                    .clickable { onBackPressed.invoke() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
        TextLarge(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = AppTheme.colors.black,
            textAlign = TextAlign.Center
        )
    }
}

@AppPreview
@Composable
fun ToolBarWidgetPreview() {
    AppThemeComposable {
        Column {
            ToolBarWidget(title = "Test Toolbar text")
        }
    }
}

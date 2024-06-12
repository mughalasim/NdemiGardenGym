package com.ndemi.garden.gym.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen

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

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ToolBarWidgetPreviewNight() {
    AppThemeComposable {
        Column {
            ToolBarWidget(
                title = "Test Long toolbar title that may over flow",
            )
        }
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun ToolBarWidgetPreview() {
    AppThemeComposable {
        Column {
            ToolBarWidget(title = "Test Toolbar text")
        }
    }
}

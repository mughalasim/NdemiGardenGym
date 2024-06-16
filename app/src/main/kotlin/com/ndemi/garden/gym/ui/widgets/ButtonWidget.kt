package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean,
    isLoading: Boolean = false,
    onButtonClicked: () -> Unit,
) {
    Row(
        modifier =
        modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .background(
                color =
                if (isEnabled && !isLoading) {
                    AppTheme.colors.backgroundButtonEnabled
                } else {
                    AppTheme.colors.backgroundButtonDisabled
                },
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen)
            .clickable {
                if (isEnabled && !isLoading) onButtonClicked()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier.width(icon_image_size).height(icon_image_size),
                strokeCap = StrokeCap.Round)
            Spacer(modifier = Modifier.padding(padding_screen_small))
        }
        TextSmall(
            text = title.uppercase(),
            color = if (isEnabled && !isLoading) AppTheme.colors.backgroundScreen else AppTheme.colors.textSecondary,
        )
    }
}

@AppPreview
@Composable
fun ButtonWidgetPreview() {
    AppThemeComposable {
        Column {
            ButtonWidget(title = "Enabled button", isEnabled = true) {}
            ButtonWidget(title = "Disabled button", isEnabled = false) {}
        }
    }
}

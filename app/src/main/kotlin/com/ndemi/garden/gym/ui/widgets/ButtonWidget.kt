package com.ndemi.garden.gym.ui.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onButtonClicked: () -> Unit,
) {
    val bgColor: Color by animateColorAsState(
        targetValue = if (isEnabled && !isLoading) {
            AppTheme.colors.backgroundButtonEnabled
        } else {
            AppTheme.colors.backgroundButtonDisabled
        },
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = ""
    )
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(top = padding_screen)
            .background(
                bgColor,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen)
            .clickable {
                if (isEnabled && !isLoading) onButtonClicked()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = padding_screen)
                    .width(icon_image_size)
                    .height(icon_image_size),
                strokeCap = StrokeCap.Round,
                trackColor = AppTheme.colors.highLight
            )
        }
        TextRegular(
            modifier = Modifier.wrapContentWidth(),
            text = title,
            color = if (isEnabled && !isLoading) {
                AppTheme.colors.backgroundScreen
            } else {
                AppTheme.colors.textSecondary
            },
        )
    }
}

@Composable
fun ButtonOutlineWidget(
    text: String,
    modifier: Modifier = Modifier,
    hasOutline: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    onButtonClicked: () -> Unit = {}
){
    OutlinedButton(
        modifier = modifier,
        onClick = { onButtonClicked.invoke() },
        shape = RoundedCornerShape(border_radius),
        border = BorderStroke(
            width = line_thickness,
            color = if (hasOutline) AppTheme.colors.highLight else Color.Transparent
        ),
        contentPadding = PaddingValues(padding_screen_tiny),
        colors = ButtonDefaults.outlinedButtonColors()
            .copy(containerColor = backgroundColor)
    ) {
        TextSmall(text = text)
    }
}

@AppPreview
@Composable
fun ButtonWidgetPreview() {
    AppThemeComposable {
        Column {
            ButtonWidget(title = "Enabled button", isEnabled = true, isLoading = true) {}
            ButtonWidget(title = "Disabled button", isEnabled = false) {}
            ButtonOutlineWidget(text = "Text button") {}
            ButtonOutlineWidget(text = "Text button",
                hasOutline = false,
                backgroundColor = AppTheme.colors.backgroundError) {}
        }
    }
}

package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun RoundedIconWidget(
    icon: ImageVector,
    onClickListener: () -> Unit,
    tintColor: Color,
) {
    Image(
        imageVector = icon,
        contentDescription = "",
        contentScale = ContentScale.Inside,
        colorFilter = ColorFilter.tint(tintColor),
        modifier =
            Modifier
                .width(icon_size_small)
                .height(icon_size_small)
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(percent = 100),
                ).border(
                    width = line_thickness,
                    color = tintColor,
                    shape = RoundedCornerShape(percent = 100),
                ).padding(padding_screen_tiny)
                .clickable {
                    onClickListener.invoke()
                },
    )
}

@Composable
@AppPreview
private fun RoundedIconWidgetPreview() =
    RoundedIconWidget(
        icon = Icons.Rounded.CameraAlt,
        onClickListener = {},
        tintColor = AppTheme.colors.textPrimary,
    )

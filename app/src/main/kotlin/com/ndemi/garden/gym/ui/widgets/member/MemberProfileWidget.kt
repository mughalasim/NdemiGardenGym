package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size_large
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun MemberProfileWidget(
    imageUrl: String,
    onImageSelect: () -> Unit = {},
    onImageDelete: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(bottom = padding_screen_large)
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        AsyncImageWidget(profileImageUrl = imageUrl, isLarge = true)
        Row(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .width(icon_image_size_profile),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                imageVector = Icons.Rounded.CameraAlt,
                contentDescription = "Select Picture",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .clip(RoundedCornerShape(border_radius))
                    .border(
                        border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                        shape = RoundedCornerShape(border_radius)
                    )
                    .background(AppTheme.colors.highLight)
                    .width(icon_image_size_large)
                    .height(icon_image_size_large)
                    .clickable {
                        onImageSelect.invoke()
                    }
            )

            if (imageUrl.isNotEmpty()) {
                Image(
                    imageVector = Icons.Rounded.DeleteForever,
                    contentDescription = "Delete Picture",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(AppTheme.colors.backgroundError),
                    modifier = Modifier
                        .clip(RoundedCornerShape(border_radius))
                        .border(
                            border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                            shape = RoundedCornerShape(border_radius)
                        )
                        .background(AppTheme.colors.highLight)
                        .width(icon_image_size_large)
                        .height(icon_image_size_large)
                        .clickable {
                            onImageDelete.invoke()
                        }
                )
            }
        }
    }
}


@AppPreview
@Composable
fun MemberProfileWidgetPreview() {
    AppThemeComposable {
        MemberProfileWidget(imageUrl = "some url goes here")
    }
}

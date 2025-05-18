package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.image_size_medium
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget

@Composable
fun MemberImageWidget(
    canEditImage: Boolean = true,
    imageUrl: String,
    onImageSelect: () -> Unit = {},
    onImageDelete: () -> Unit = {},
) {
    Box(
        modifier = Modifier.wrapContentSize(),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .size(image_size_medium)
                    .clip(RoundedCornerShape(percent = 100))
                    .background(AppTheme.colors.backgroundCard)
                    .border(
                        width = line_thickness,
                        color = AppTheme.colors.backgroundScreen,
                        shape = RoundedCornerShape(percent = 100),
                    ),
            profileImageUrl = imageUrl,
        )
        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd),
        ) {
            if (canEditImage) {
                if (imageUrl.isNotEmpty()) {
                    ImageIcon(Icons.Rounded.DeleteForever, onImageDelete, AppTheme.colors.error)
                } else {
                    ImageIcon(Icons.Rounded.CameraAlt, onImageSelect, AppTheme.colors.textPrimary)
                }
            }
        }
    }
}

@Composable
private fun ImageIcon(
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
                )
                .border(
                    width = line_thickness,
                    color = AppTheme.colors.backgroundScreen,
                    shape = RoundedCornerShape(percent = 100),
                )
                .padding(padding_screen_tiny)
                .clickable {
                    onClickListener.invoke()
                },
    )
}

@AppPreview
@Composable
private fun MemberProfileWidgetPreview() {
    AppThemeComposable {
        MemberImageWidget(imageUrl = "some url goes here")
    }
}

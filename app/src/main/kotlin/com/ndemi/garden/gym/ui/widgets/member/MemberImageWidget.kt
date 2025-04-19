package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_image_size_large
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget

@Composable
fun MemberImageWidget(
    isEnabled: Boolean = true,
    imageUrl: String,
    onImageSelect: () -> Unit = {},
    onImageDelete: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .padding(bottom = padding_screen)
                .wrapContentHeight(),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(icon_image_size_profile),
            profileImageUrl = imageUrl,
        )
        Row(
            modifier =
                Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(padding_screen),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (isEnabled) {
                if (imageUrl.isNotEmpty()) {
                    ImageIcon(Icons.Rounded.DeleteForever, onImageDelete, AppTheme.colors.error)
                }
                ImageIcon(Icons.Rounded.CameraAlt, onImageSelect, AppTheme.colors.textPrimary)
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
                .width(icon_image_size_large)
                .height(icon_image_size_large)
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

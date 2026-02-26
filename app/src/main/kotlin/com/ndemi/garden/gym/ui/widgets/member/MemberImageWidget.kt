package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.image_size_small
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget

@Composable
fun MemberImageWidget(
    modifier: Modifier = Modifier,
    imageUrl: String,
    canEditImage: Boolean = true,
    overrideImageSize: Dp = image_size_small,
    onImageSelect: () -> Unit = {},
    onImageDelete: () -> Unit = {},
) {
    Box(
        modifier = modifier.wrapContentSize(),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .size(overrideImageSize)
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
                    RoundedIconWidget(Icons.Rounded.DeleteForever, onImageDelete, AppTheme.colors.error)
                } else {
                    RoundedIconWidget(Icons.Rounded.CameraAlt, onImageSelect, AppTheme.colors.textPrimary)
                }
            }
        }
    }
}

@AppPreview
@Composable
private fun MemberProfileWidgetPreview() {
    AppThemeComposable {
        MemberImageWidget(imageUrl = "some url goes here")
    }
}

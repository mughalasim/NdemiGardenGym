package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget

@Composable
fun MemberImageFullWidget(
    modifier: Modifier = Modifier,
    imageUrl: String,
    canEditImage: Boolean = true,
    onImageSelect: () -> Unit = {},
    onImageDelete: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = border_radius,
                            bottomEnd = border_radius,
                        ),
                    ).height(image_size_large)
                    .background(
                        color = AppTheme.colors.backgroundCard,
                        shape =
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = border_radius,
                                bottomEnd = border_radius,
                            ),
                    ),
            profileImageUrl = imageUrl,
        )
        Row(
            modifier =
                Modifier
                    .padding(padding_screen)
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
        Column {
            MemberImageFullWidget(imageUrl = "some url goes here")
            MemberImageFullWidget(imageUrl = "")
        }
    }
}

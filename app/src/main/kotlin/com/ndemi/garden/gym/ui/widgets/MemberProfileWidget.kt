package com.ndemi.garden.gym.ui.widgets

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
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
            .padding(vertical = padding_screen_large)
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_app_foreground),
            fallback = painterResource(R.drawable.ic_app_foreground),
            error = painterResource(R.drawable.ic_app_foreground),
            contentDescription = "profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                    shape = CircleShape
                )
                .width(icon_image_size_profile)
                .height(icon_image_size_profile)
        )
        Row(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .width(icon_image_size_profile),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Select Picture",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                        shape = CircleShape
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
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete Picture",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                            shape = CircleShape
                        )
                        .background(AppTheme.colors.backgroundError)
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
        MemberProfileWidget(imageUrl = "tyutu")
    }
}
package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile_small
import com.ndemi.garden.gym.ui.theme.line_thickness

@Composable
fun AsyncImageWidget(
    profileImageUrl: String,
    isLarge: Boolean = false,
){
    val dimen = if(isLarge) icon_image_size_profile else icon_image_size_profile_small
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImageUrl)
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
            .width(dimen)
            .height(dimen)
    )
}
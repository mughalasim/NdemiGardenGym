package com.ndemi.garden.gym.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndemi.garden.gym.R

@Composable
fun AsyncImageWidget(
    modifier: Modifier = Modifier,
    profileImageUrl: String,
) {
    AsyncImage(
        model =
            ImageRequest.Builder(LocalContext.current)
                .data(profileImageUrl)
                .crossfade(true)
                .build(),
        placeholder = painterResource(R.drawable.ic_app),
        fallback = painterResource(R.drawable.ic_app),
        error = painterResource(R.drawable.ic_app),
        contentDescription = "profile picture",
        contentScale = ContentScale.FillHeight,
        modifier = modifier,
    )
}

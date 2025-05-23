package com.ndemi.garden.gym.ui.widgets.member

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.image_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import org.joda.time.DateTime

@Composable
fun MemberSessionWidget(
    sessionStartTime: Long? = null,
    countdown: String = "",
    onSessionTapped: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(top = padding_screen)
                .toAppCardStyle(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextWidget(
                text = stringResource(R.string.txt_workout_session),
                style = AppTheme.textStyles.large,
            )
            ButtonWidget(
                modifier =
                    Modifier
                        .clickable { onSessionTapped.invoke() },
                title =
                    if (sessionStartTime != null) {
                        stringResource(R.string.txt_stop)
                    } else {
                        stringResource(R.string.txt_start)
                    },
                isOutlined = true,
                overridePadding = padding_screen_small,
                onButtonClicked = onSessionTapped,
            )
        }

        if (sessionStartTime != null) {
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = padding_screen)
                        .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GifImage()
                Column {
                    TextWidget(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = padding_screen_small),
                        color = AppTheme.colors.primary,
                        textAlign = TextAlign.Center,
                        text =
                            stringResource(
                                R.string.txt_started_at,
                                DateTime(sessionStartTime).toString(DateConstants.formatTime),
                            ),
                    )
                    TextWidget(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = padding_screen_small),
                        text = countdown,
                        textAlign = TextAlign.Center,
                        style = AppTheme.textStyles.large,
                    )
                }
            }
        } else {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(R.string.txt_workout_session_desc),
            )
        }
    }
}

@Composable
private fun GifImage() {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    Image(
        modifier =
            Modifier
                .size(image_size_small)
                .clip(shape = CircleShape),
        painter =
            rememberAsyncImagePainter(
                model =
                    ImageRequest
                        .Builder(context)
                        .data(data = R.drawable.running)
                        .apply(block = { size(Size.ORIGINAL) })
                        .build(),
                imageLoader = imageLoader,
            ),
        contentDescription = null,
    )
}

@AppPreview
@Composable
private fun MemberSessionWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberSessionWidget(sessionStartTime = 35435135151L)
            MemberSessionWidget(sessionStartTime = null)
        }
    }
}

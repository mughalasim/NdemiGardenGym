package com.ndemi.garden.gym.ui.widgets

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cv.domain.entities.LinkEntity
import cv.domain.entities.getFakeLinks
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen_small

@Composable
fun ChipWidget(
    modifier: Modifier = Modifier,
    entity: LinkEntity,
    onLinkTapped: (String) -> Unit,
    context: Context = LocalContext.current,
) {
    Surface(
        modifier =
            modifier
                .padding(end = padding_screen_small, top = padding_screen_small)
                .border(
                    width = line_thickness_small,
                    color = AppTheme.colors.backgroundChip,
                    shape = RoundedCornerShape(border_radius),
                ),
        color = Color.Transparent,
    ) {
        Row(
            modifier =
                Modifier
                    .toggleable(
                        value = false,
                        onValueChange = {
                            val url = entity.url
                            if (URLUtil.isValidUrl(url)) {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(browserIntent)
                                onLinkTapped(url)
                            } else {
                                Toast.makeText(
                                    context,
                                    context.resources.getString(R.string.error_invalid_link),
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
                        },
                    ),
        ) {
            TextRegular(
                modifier = Modifier.padding(padding_screen_small),
                text = entity.text,
            )
        }
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ChipWidgetPreviewNight() {
    AppThemeComposable {
        ChipWidget(entity = getFakeLinks()[0], onLinkTapped = {})
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun ChipWidgetPreview() {
    AppThemeComposable {
        ChipWidget(entity = getFakeLinks()[0], onLinkTapped = {})
    }
}
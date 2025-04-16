package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun NewVersionScreen(url: String) {
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .requiredWidth(page_width)
                .padding(padding_screen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val uriHandler = LocalUriHandler.current
        TextWidget(
            text = stringResource(R.string.txt_app_update_title),
            style = AppTheme.textStyles.large,
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen),
            textAlign = TextAlign.Center,
            text =
                stringResource(R.string.txt_app_update_desc),
        )
        if (isValidUri(url)) {
            ButtonWidget(
                modifier = Modifier.padding(top = padding_screen),
                title = stringResource(R.string.txt_download),
            ) {
                uriHandler.openUri(url)
            }
        }
    }
}

fun isValidUri(uriString: String): Boolean {
    val uri = runCatching { uriString.toUri() }.getOrNull()
    return uri != null
}

@AppPreview
@Composable
private fun NewVersionScreenPreview() {
    AppThemeComposable {
        NewVersionScreen(url = "https://www.google.com")
    }
}

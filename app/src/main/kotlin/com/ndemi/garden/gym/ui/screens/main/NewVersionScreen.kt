package com.ndemi.garden.gym.ui.screens.main

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget


@Composable
fun NewVersionScreen(
    url: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uriHandler = LocalUriHandler.current
        val context = LocalContext.current
        TextWidget(
            text = stringResource(R.string.txt_app_update_title),
            style = AppTheme.textStyles.regularBold,
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen),
            text =
            stringResource(R.string.txt_app_update_desc)
        )
        ButtonWidget(title = stringResource(R.string.txt_download)) {
            if (isValidUri(url)) {
                uriHandler.openUri(url)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_failed_to_open_link), Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Suppress("TooGenericExceptionCaught")
fun isValidUri(uriString: String): Boolean {
    return try {
        val uri = Uri.parse(uriString)
        uri.scheme != null && uri.host != null
    } catch (e: Exception) {
        Log.e("isValidUri", e.message?: "IllegalArgumentException")
        false
    }
}

@AppPreview
@Composable
private fun NewVersionScreenPreview() {
    AppThemeComposable {
        NewVersionScreen(url = "SampleUrl")
    }
}

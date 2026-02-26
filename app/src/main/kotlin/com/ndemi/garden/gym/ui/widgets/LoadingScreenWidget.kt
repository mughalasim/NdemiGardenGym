package com.ndemi.garden.gym.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun LoadingScreenWidget(
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier.fillMaxSize(),
    customMessage: String = "Loading... please wait...",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .width(icon_size_small)
                    .height(icon_size_small),
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen),
            text = customMessage,
        )
    }
}

@AppPreview
@Composable
private fun LoadingScreenWidgetPreview() {
    AppThemeComposable {
        LoadingScreenWidget()
    }
}

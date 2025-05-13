package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun MessageScreen(
    title: String,
    message: String,
    buttonText: String,
    onButtonTapped: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .requiredWidth(page_width)
                .toAppCardStyle()
                .padding(vertical = padding_screen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextWidget(
            text = title,
            style = AppTheme.textStyles.large,
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen),
            textAlign = TextAlign.Center,
            text = message,
        )
        if (buttonText.isNotEmpty()) {
            ButtonWidget(
                modifier = Modifier.padding(top = padding_screen_large),
                title = buttonText,
                onButtonClicked = onButtonTapped,
            )
        }
    }
}

@AppPreview
@Composable
private fun MessageScreenPreview() {
    AppThemeComposable {
        MessageScreen(
            title = "Title",
            message = "Some important message will be here",
            buttonText = "Exit",
        ) {}
    }
}

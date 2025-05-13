package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle

@Composable
fun VerifyEmailWidget(onVerifyTapped: () -> Unit) {
    Row(
        modifier =
            Modifier
                .padding(vertical = padding_screen_tiny)
                .padding(horizontal = padding_screen),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextWidget(
            modifier = Modifier.weight(MESSAGE_SIZE).padding(end = padding_screen),
            style = AppTheme.textStyles.small,
            text = stringResource(R.string.error_email_not_verified),
        )
        TextWidget(
            modifier =
                Modifier
                    .weight(1f)
                    .toAppCardStyle()
                    .clickable { onVerifyTapped.invoke() },
            style = AppTheme.textStyles.small,
            text = "Verify now",
            textAlign = TextAlign.Center,
        )
    }
}

private const val MESSAGE_SIZE = 3f

@AppPreview
@Composable
private fun VerifyEmailWidgetPreview() =
    AppThemeComposable {
        VerifyEmailWidget {}
    }

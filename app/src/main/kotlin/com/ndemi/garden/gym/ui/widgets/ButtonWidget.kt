package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isOutlined: Boolean = false,
    hideKeyboardOnClick: Boolean = false,
    overridePadding: Dp = padding_screen,
    onButtonClicked: () -> Unit = {},
) {
    var backgroundColor: Color = AppTheme.colors.backgroundButtonDisabled
    var outlineColor: Color = Color.Transparent
    var textColor: Color = AppTheme.colors.textSecondary

    when {
        isLoading -> {
            outlineColor = Color.Transparent
            backgroundColor = AppTheme.colors.backgroundButtonDisabled
            textColor = Color.Transparent
        }
        isOutlined && isEnabled -> {
            outlineColor = AppTheme.colors.primary
            backgroundColor = Color.Transparent
            textColor = AppTheme.colors.primary
        }
        isOutlined && !isEnabled -> {
            outlineColor = AppTheme.colors.backgroundButtonDisabled
            backgroundColor = Color.Transparent
            textColor = AppTheme.colors.textSecondary
        }
        !isOutlined && isEnabled -> {
            outlineColor = Color.Transparent
            backgroundColor = AppTheme.colors.backgroundButtonEnabled
            textColor = AppTheme.colors.textPrimary
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier =
            modifier
                .clickable {
                    if (hideKeyboardOnClick) keyboardController?.hide()
                    if (isEnabled && !isLoading) onButtonClicked()
                }
                .background(
                    if (!isOutlined) backgroundColor else Color.Transparent,
                    shape = RoundedCornerShape(border_radius),
                )
                .border(
                    width = line_thickness,
                    color = outlineColor,
                    shape = RoundedCornerShape(border_radius),
                )
                .padding(overridePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .width(icon_size_small)
                        .height(icon_size_small),
                strokeCap = StrokeCap.Round,
                trackColor = AppTheme.colors.primary,
            )
        } else {
            TextWidget(
                text = title,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@AppPreview
@Composable
private fun ButtonWidgetPreview() {
    AppThemeComposable {
        Column {
            ButtonWidget(title = "Loading", isLoading = true)
            Spacer(modifier = Modifier.padding(padding_screen))
            ButtonWidget(title = "Enabled button", isEnabled = true)
            Spacer(modifier = Modifier.padding(padding_screen))
            ButtonWidget(title = "Disabled button", isEnabled = false)
            Spacer(modifier = Modifier.padding(padding_screen))
            ButtonWidget(title = "Enabled Outlined button", isOutlined = true)
            Spacer(modifier = Modifier.padding(padding_screen))
            ButtonWidget(title = "Disabled Outlined button", isEnabled = false, isOutlined = true)
        }
    }
}

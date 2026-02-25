package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_size_large
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun DateSelectionWidget(
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.txt_selected_year),
    selectedText: String,
    isLoading: Boolean = false,
    onMinusTapped: () -> Unit = {},
    onPlusTapped: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .padding(top = padding_screen_small)
                .wrapContentHeight()
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(border_radius),
                ).padding(padding_screen_small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier =
                Modifier
                    .size(icon_size_small)
                    .clickable { onMinusTapped.invoke() },
            tint = AppTheme.colors.primary,
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            contentDescription = null,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(icon_size_large),
                    color = AppTheme.colors.primary,
                )
            } else {
                TextWidget(
                    text = label,
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    text = selectedText,
                    style = AppTheme.textStyles.regularBold,
                )
            }
        }
        Icon(
            modifier =
                Modifier
                    .size(icon_size_small)
                    .clickable { onPlusTapped.invoke() },
            tint = AppTheme.colors.primary,
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
        )
    }
}

@AppPreview
@Composable
private fun DateSelectionWidgetPreview() {
    AppThemeComposable {
        Row(modifier = Modifier.fillMaxWidth()) {
            DateSelectionWidget(
                modifier = Modifier.weight(1f),
                label = "Selected Year",
                selectedText = "2023",
            )
            Spacer(modifier = Modifier.padding(start = padding_screen_small))
            DateSelectionWidget(
                modifier = Modifier.weight(1f),
                label = "Selected Month",
                selectedText = "September",
            )
        }
    }
}

package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.ndemi.garden.gym.ui.theme.icon_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle

@Composable
fun YearSelectionWidget(
    selectedYear: String,
    isLoading: Boolean = false,
    onYearMinusTapped: () -> Unit = {},
    onYearPlusTapped: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .padding(padding_screen)
                .toAppCardStyle(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier =
                Modifier
                    .size(icon_size_large)
                    .clickable { onYearMinusTapped.invoke() },
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
                    text = stringResource(R.string.txt_selected_year),
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    text = selectedYear,
                    style = AppTheme.textStyles.large,
                )
            }
        }
        Icon(
            modifier =
                Modifier
                    .size(icon_size_large)
                    .clickable { onYearPlusTapped.invoke() },
            tint = AppTheme.colors.primary,
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
        )
    }
}

@AppPreview
@Composable
private fun AttendanceDateSelectionWidgetPreview() {
    AppThemeComposable {
        YearSelectionWidget(
            selectedYear = "2025",
        ) {}
    }
}

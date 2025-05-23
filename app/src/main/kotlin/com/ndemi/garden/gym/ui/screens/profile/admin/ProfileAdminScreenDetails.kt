package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.profile.ProfileTopSection
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_medium
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun ProfileAdminScreenDetails(onLogoutTapped: () -> Unit = {}) {
    Column {
        ProfileTopSection(onLogoutTapped = onLogoutTapped)
        Column(
            modifier =
                Modifier
                    .padding(horizontal = padding_screen)
                    .verticalScroll(rememberScrollState()),
        ) {
            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "82",
                    description = "Total registered Users",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "20",
                    description = "Not renewed Membership",
                )
            }

            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "Kes 51,250",
                    description = "Total revenue this year",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "Kes 2,500",
                    description = "Total revenue this month",
                )
            }

            Column(
                Modifier
                    .padding(top = padding_screen)
                    .toAppCardStyle(),
            ) {
                TextWidget(
                    style = AppTheme.textStyles.regularBold,
                    text = "Top 10 paying members",
                )
                TextWidget(
                    modifier = Modifier.padding(vertical = padding_screen),
                    text = "No members found",
                )
            }
            Column(
                Modifier
                    .padding(top = padding_screen)
                    .toAppCardStyle(),
            ) {
                TextWidget(
                    style = AppTheme.textStyles.regularBold,
                    text = "Top 10 active members",
                )
                TextWidget(
                    modifier = Modifier.padding(vertical = padding_screen),
                    text = "No members found",
                )
            }
        }
    }
}

@Composable
private fun Tile(
    modifier: Modifier,
    value: String = "",
    description: String = "",
) {
    Box(
        modifier =
            modifier
                .height(image_size_medium)
                .padding(top = padding_screen)
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(border_radius),
                )
                .padding(padding_screen_small),
    ) {
        TextWidget(
            text = value,
            color = AppTheme.colors.primary,
            style = AppTheme.textStyles.largeExtra,
        )
        TextWidget(
            modifier = Modifier.align(Alignment.BottomStart),
            style = AppTheme.textStyles.regularBold,
            text = description,
        )
    }
}

@AppPreview
@Composable
private fun ProfileAdminScreenDetailsPreview() =
    AppThemeComposable {
        ProfileAdminScreenDetails()
    }

package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.screens.profile.ProfileTopSection
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.AdminDashboard

@Composable
fun ProfileAdminScreenDetails(
    state: AdminDashboard = AdminDashboard(),
    listeners: ProfileAdminScreenDetailsListeners = ProfileAdminScreenDetailsListeners(),
) {
    Column {
        ProfileTopSection(onLogoutTapped = listeners.onLogoutTapped)

        Column(
            modifier =
                Modifier
                    .padding(horizontal = padding_screen)
                    .verticalScroll(rememberScrollState()),
        ) {
            Row {
                DateSelectionWidget(
                    modifier = Modifier.weight(1f),
                    selectedText = state.selectedYear.toString(),
                    label = stringResource(R.string.txt_selected_year),
                    onPlusTapped = listeners.onYearPlusTapped,
                    onMinusTapped = listeners.onYearMinusTapped,
                )
                Spacer(modifier = Modifier.padding(start = padding_screen_small))
                DateSelectionWidget(
                    modifier = Modifier.weight(1f),
                    selectedText = state.selectedMonth,
                    label = stringResource(R.string.txt_selected_month),
                    onPlusTapped = listeners.onMonthPlusTapped,
                    onMinusTapped = listeners.onMonthMinusTapped,
                )
            }

            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = state.totalRegisteredUsers.toString(),
                    description = "Total users",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen_small))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = state.totalExpiredUsers.toString(),
                    description = "Expired memberships",
                )
            }

            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = currencyFormater(state.totalRevenueYear),
                    description = "Revenue for ${state.selectedYear}",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen_small))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = currencyFormater(state.totalRevenueMonth),
                    description = "Revenue for ${state.selectedMonth}",
                )
            }

            Column(
                Modifier
                    .padding(top = padding_screen_small)
                    .toAppCardStyle(),
            ) {
                TextWidget(
                    style = AppTheme.textStyles.regularBold,
                    text = "Top 10 paying members for ${state.selectedYear}",
                )
                if (state.topTenPayingMembers.isEmpty()) {
                    TextWidget(
                        modifier = Modifier.padding(vertical = padding_screen_small),
                        text = "No members found",
                    )
                } else {
                    for (member in state.topTenPayingMembers) {
                        MemberInfoStat(member.first.getFullName(), currencyFormater(member.second))
                    }
                }
            }
            Column(
                Modifier
                    .padding(top = padding_screen_small)
                    .toAppCardStyle(),
            ) {
                TextWidget(
                    style = AppTheme.textStyles.regularBold,
                    text = "Top 10 active members for ${state.selectedMonth}",
                )
                if (state.topTenActiveMembers.isEmpty()) {
                    TextWidget(
                        modifier = Modifier.padding(vertical = padding_screen),
                        text = "No members found",
                    )
                } else {
                    for (member in state.topTenActiveMembers) {
                        MemberInfoStat(member.first.getFullName(), "${member.second} visits")
                    }
                }
            }
        }
    }
}

private fun currencyFormater(value: Double): String {
    val formattedAmount = String.format(locale = null, "%,.2f", value)
    return "${BuildConfig.CURRENCY_CODE} $formattedAmount"
}

@Composable
private fun MemberInfoStat(
    name: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TextWidget(text = name)
        TextWidget(text = value, color = AppTheme.colors.primary)
    }
}

data class ProfileAdminScreenDetailsListeners(
    val onLogoutTapped: () -> Unit = {},
    val onYearPlusTapped: () -> Unit = {},
    val onYearMinusTapped: () -> Unit = {},
    val onMonthPlusTapped: () -> Unit = {},
    val onMonthMinusTapped: () -> Unit = {},
)

@Composable
private fun Tile(
    modifier: Modifier,
    value: String = "",
    description: String = "",
) {
    Box(
        modifier =
            modifier
                .height(image_size_small)
                .padding(top = padding_screen_small)
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(border_radius),
                ).padding(padding_screen_small),
    ) {
        TextWidget(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = padding_screen),
            text = value,
            color = AppTheme.colors.primary,
            style = AppTheme.textStyles.regularBold,
        )
        TextWidget(
            modifier = Modifier.align(Alignment.BottomCenter),
            textAlign = TextAlign.Center,
            style = AppTheme.textStyles.regularBold,
            text = description,
        )
    }
}

@AppPreview
@Composable
@Suppress("detekt.MagicNumber")
private fun ProfileAdminScreenDetailsPreview() =
    AppThemeComposable {
        ProfileAdminScreenDetails(
            state =
                AdminDashboard(
                    totalRegisteredUsers = 82,
                    totalExpiredUsers = 20,
                    totalRevenueMonth = 123658.0,
                    totalRevenueYear = 1524435.54,
                    topTenActiveMembers =
                        listOf(
                            Pair(getMockActiveMemberEntity(), 2),
                            Pair(getMockActiveMemberEntity(), 1),
                        ),
                    topTenPayingMembers =
                        listOf(
                            Pair(getMockActiveMemberEntity(), 2000.0),
                            Pair(getMockActiveMemberEntity(), 1000.0),
                        ),
                ),
        )
    }

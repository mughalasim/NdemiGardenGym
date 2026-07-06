package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_small
import com.ndemi.garden.gym.ui.theme.image_size_tiny
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.presentationModels.ProfileAdminMemberNavigationType
import cv.domain.presentationModels.TopTenMemberPresentationModel

@Composable
fun ProfileAdminScreenDetails(
    uiState: UiState,
    listeners: ProfileAdminScreenDetailsListeners = ProfileAdminScreenDetailsListeners(),
) {
    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_profile),
            secondaryIcon = Icons.Default.Settings,
            onSecondaryIconPressed = listeners.onSettingsTapped,
        )

        when (uiState) {
            is UiState.Loading -> {
                LoadingScreenWidget()
            }

            is UiState.Success -> {
                Column(
                    modifier =
                        Modifier
                            .padding(horizontal = padding_screen)
                            .verticalScroll(rememberScrollState()),
                ) {
                    Row {
                        DateSelectionWidget(
                            modifier = Modifier.weight(1f),
                            selectedText = uiState.model.selectedYear.toString(),
                            label = stringResource(R.string.txt_selected_year),
                            onPlusTapped = listeners.onYearPlusTapped,
                            onMinusTapped = listeners.onYearMinusTapped,
                        )
                        Spacer(modifier = Modifier.padding(start = padding_screen_small))
                        DateSelectionWidget(
                            modifier = Modifier.weight(1f),
                            selectedText = uiState.model.selectedMonth,
                            label = stringResource(R.string.txt_selected_month),
                            onPlusTapped = listeners.onMonthPlusTapped,
                            onMinusTapped = listeners.onMonthMinusTapped,
                        )
                    }

                    Row {
                        Tile(
                            modifier = Modifier.weight(1f),
                            value = uiState.model.totalRegisteredUsers.toString(),
                            description = "Total users",
                        )
                        Spacer(modifier = Modifier.padding(start = padding_screen_small))
                        Tile(
                            modifier = Modifier.weight(1f),
                            value = uiState.model.totalExpiredUsers.toString(),
                            description = "Expired memberships",
                        )
                    }

                    Row {
                        Tile(
                            modifier = Modifier.weight(1f),
                            value = uiState.model.totalRevenueYear,
                            description = "Revenue for ${uiState.model.selectedYear}",
                        )
                        Spacer(modifier = Modifier.padding(start = padding_screen_small))
                        Tile(
                            modifier = Modifier.weight(1f),
                            value = uiState.model.totalRevenueMonth,
                            description = "Revenue for ${uiState.model.selectedMonth}",
                        )
                    }

                    Column(
                        Modifier
                            .padding(top = padding_screen_small)
                            .toAppCardStyle(),
                    ) {
                        TextWidget(
                            style = AppTheme.textStyles.regularBold,
                            text = "Top 10 paying members for ${uiState.model.selectedYear}",
                        )
                        if (uiState.model.topTenPayingMembers.isEmpty()) {
                            TextWidget(
                                modifier = Modifier.padding(vertical = padding_screen_small),
                                text = "No members found",
                            )
                        } else {
                            for (member in uiState.model.topTenPayingMembers) {
                                MemberInfoStat(
                                    profileImageUrl = member.image,
                                    name = member.fullName,
                                    value = member.amountFormatted,
                                    onClick = {
                                        listeners.onMemberTapped(
                                            member,
                                            uiState.model.selectedYear,
                                            ProfileAdminMemberNavigationType.PAYMENT,
                                        )
                                    },
                                )
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
                            text = "Top 10 active members for ${uiState.model.selectedMonth}",
                        )
                        if (uiState.model.topTenActiveMembers.isEmpty()) {
                            TextWidget(
                                modifier = Modifier.padding(vertical = padding_screen),
                                text = "No members found",
                            )
                        } else {
                            for (member in uiState.model.topTenActiveMembers) {
                                MemberInfoStat(
                                    profileImageUrl = member.image,
                                    name = member.fullName,
                                    value = "${member.visits} visits",
                                    onClick = {
                                        listeners.onMemberTapped(
                                            member,
                                            uiState.model.selectedYear,
                                            ProfileAdminMemberNavigationType.ATTENDANCE,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberInfoStat(
    profileImageUrl: String,
    name: String,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clickable(enabled = true, onClick = onClick)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .size(image_size_tiny)
                    .border(width = line_thickness, color = AppTheme.colors.border, shape = RoundedCornerShape(image_size_tiny)),
            profileImageUrl = profileImageUrl,
        )
        TextWidget(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = padding_screen_small),
            text = name,
        )
        TextWidget(text = value, color = AppTheme.colors.primary)
    }
}

data class ProfileAdminScreenDetailsListeners(
    val onSettingsTapped: () -> Unit = {},
    val onYearPlusTapped: () -> Unit = {},
    val onYearMinusTapped: () -> Unit = {},
    val onMonthPlusTapped: () -> Unit = {},
    val onMonthMinusTapped: () -> Unit = {},
    val onMemberTapped: (TopTenMemberPresentationModel, Int, ProfileAdminMemberNavigationType) -> Unit = { _, _, _ -> },
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
            uiState =
                UiState.Success(
                    model =
                        AdminDashboardPresentationModel(
                            selectedYear = 2025,
                            selectedMonth = "January",
                            totalRegisteredUsers = 82,
                            totalExpiredUsers = 20,
                            totalRevenueMonth = "KES 123,658.0",
                            totalRevenueYear = "KES 1,524,435.54",
                            topTenActiveMembers =
                                listOf(
                                    TopTenMemberPresentationModel(
                                        id = "123",
                                        image = "TODO()",
                                        fullName = "Asim Mughal",
                                        visits = 3,
                                        amountFormatted = "£ 3,000",
                                        amountValue = 3000.0,
                                    ),
                                    TopTenMemberPresentationModel(
                                        id = "123",
                                        image = "TODO()",
                                        fullName = "Asim Test",
                                        visits = 3,
                                        amountFormatted = "£ 4,000",
                                        amountValue = 3000.0,
                                    ),
                                ),
                            topTenPayingMembers =
                                listOf(
                                    TopTenMemberPresentationModel(
                                        id = "123",
                                        image = "TODO()",
                                        fullName = "Asim Mughal",
                                        visits = 3,
                                        amountFormatted = "£ 3,000",
                                        amountValue = 3000.0,
                                    ),
                                    TopTenMemberPresentationModel(
                                        id = "123",
                                        image = "TODO()",
                                        fullName = "Asim Test",
                                        visits = 3,
                                        amountFormatted = "£ 4,000",
                                        amountValue = 3000.0,
                                    ),
                                ),
                        ),
                ),
        )
    }

package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.screens.profile.ProfileTopSection
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_medium
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import cv.domain.entities.AdminDashboard
import org.joda.time.DateTime

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
                    .padding(top = padding_screen)
                    .padding(horizontal = padding_screen)
                    .clickable { listeners.onEditDetailsTapped.invoke() }
                    .toAppCardStyle(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MemberImageWidget(
                    imageUrl = state.memberEntity.profileImageUrl,
                    onImageDelete = listeners.onImageDeleted,
                    onImageSelect = listeners.onImageSelected,
                )
                Column(
                    modifier = Modifier.padding(horizontal = padding_screen),
                ) {
                    TextWidget(
                        text = state.memberEntity.getFullName(),
                        style = AppTheme.textStyles.large,
                    )
                    TextWidget(
                        modifier = Modifier.padding(top = padding_screen_small),
                        text =
                            stringResource(
                                R.string.txt_member_since,
                                DateTime(state.memberEntity.registrationDateMillis).toString(DateConstants.formatMonthYear),
                            ),
                    )
                }
            }
        }

        Column(
            modifier =
                Modifier
                    .padding(horizontal = padding_screen)
                    .verticalScroll(rememberScrollState()),
        ) {
            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = state.totalRegisteredUsers.toString(),
                    description = "Total registered Users",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = state.totalExpiredUsers.toString(),
                    description = "Not renewed Membership",
                )
            }

            Row {
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "${BuildConfig.CURRENCY_CODE} ${state.totalRevenueYear}",
                    description = "Total revenue this year",
                )
                Spacer(modifier = Modifier.padding(start = padding_screen))
                Tile(
                    modifier = Modifier.weight(1f),
                    value = "${BuildConfig.CURRENCY_CODE} ${state.totalRevenueMonth}",
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
                if (state.topTenPayingMembers.isEmpty()) {
                    TextWidget(
                        modifier = Modifier.padding(vertical = padding_screen),
                        text = "No members found",
                    )
                } else {
                    for (member in state.topTenPayingMembers) {
                        MemberInfoStat(member.first.getFullName(), "${BuildConfig.CURRENCY_CODE} ${member.second}")
                    }
                }
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
        TextWidget(text = value)
    }
}

data class ProfileAdminScreenDetailsListeners(
    val onImageDeleted: () -> Unit = {},
    val onImageSelected: () -> Unit = {},
    val onEditDetailsTapped: () -> Unit = {},
    val onLogoutTapped: () -> Unit = {},
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
@Suppress("detekt.MagicNumber")
private fun ProfileAdminScreenDetailsPreview() =
    AppThemeComposable {
        ProfileAdminScreenDetails(
            state =
                AdminDashboard(
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

package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberPresentationModel
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.image_size_medium
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.presentationModels.MemberPresentationModel

@Composable
fun MemberStatusWidget(
    modifier: Modifier = Modifier,
    model: MemberPresentationModel,
    canViewMemberDetails: Boolean = false,
    canViewMemberStats: Boolean = false,
    listener: MemberStatusWidgetListener = MemberStatusWidgetListener(),
) {
    Column(
        modifier =
            modifier
                .clickable { if (canViewMemberDetails) listener.onMemberTapped.invoke(model) }
                .toAppCardStyle(overridePadding = 0.dp),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(image_size_medium)
                    .clip(RoundedCornerShape(topEnd = border_radius, topStart = border_radius)),
            profileImageUrl = model.profileImageUrl,
        )

        Column(
            modifier =
                Modifier
                    .padding(padding_screen_small),
        ) {
            TextWidget(
                text = model.fullName,
                style = AppTheme.textStyles.regularBold,
            )
            if (model.isActive) {
                TextWidget(
                    style = AppTheme.textStyles.small,
                    text = model.lastActive,
                )
            } else if (canViewMemberDetails) {
                TextWidget(
                    style = AppTheme.textStyles.small,
                    text = model.residentialStatus,
                )
                if (model.hasPaidMembership) {
                    TextWidget(
                        style = AppTheme.textStyles.small,
                        modifier = Modifier.fillMaxWidth(),
                        text = model.membershipRenewalDate,
                        color = model.membershipWarningLevel.getWarningStatusColor(),
                    )
                    TextWidget(
                        style = AppTheme.textStyles.regularBold,
                        text = model.amountDue,
                        color = model.membershipWarningLevel.getWarningStatusColor(),
                    )
                }
            }
        }

        if (canViewMemberStats) {
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = padding_screen_small)
                        .padding(bottom = padding_screen_small)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                RoundedIconWidget(
                    icon = Icons.Rounded.AttachMoney,
                    onClickListener = { listener.onPaymentsTapped.invoke(model) },
                    tintColor = AppTheme.colors.primary,
                )

                RoundedIconWidget(
                    icon = Icons.Rounded.AccessTime,
                    onClickListener = { listener.onAttendanceTapped.invoke(model) },
                    tintColor = AppTheme.colors.primary,
                )

                RoundedIconWidget(
                    icon = Icons.AutoMirrored.Rounded.DirectionsRun,
                    onClickListener = { listener.onSessionTapped.invoke(model) },
                    tintColor = if (model.isActive) Color.Green else AppTheme.colors.primary,
                )
            }
        }
    }
}

data class MemberStatusWidgetListener(
    val onMemberTapped: (model: MemberPresentationModel) -> Unit = {},
    val onPaymentsTapped: (model: MemberPresentationModel) -> Unit = {},
    val onAttendanceTapped: (model: MemberPresentationModel) -> Unit = {},
    val onSessionTapped: (model: MemberPresentationModel) -> Unit = {},
)

@Composable
fun Int.getWarningStatusColor(): Color =
    when (this) {
        0 -> AppTheme.colors.textPrimary
        else -> AppTheme.colors.error
    }

@AppPreview
@Composable
private fun MemberWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberStatusWidget(
                model = getMockRegisteredMemberPresentationModel(),
                canViewMemberDetails = true,
            )
            MemberStatusWidget(
                model = getMockActiveMemberPresentationModel(),
                canViewMemberDetails = true,
                canViewMemberStats = true,
            )
            MemberStatusWidget(
                model = getMockExpiredMemberPresentationModel(),
                canViewMemberDetails = true,
                canViewMemberStats = true,
            )
            MemberStatusWidget(
                model = getMockActiveMemberPresentationModel(),
            )
        }
    }
}

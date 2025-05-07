package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.image_size_large
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.MemberEntity

@Composable
fun MemberStatusWidget(
    modifier: Modifier = Modifier,
    memberEntity: MemberEntity,
    hasAdminRights: Boolean = false,
    listener: MemberStatusWidgetListener = MemberStatusWidgetListener(),
) {
    Column(
        modifier =
            modifier
                .clickable { if (hasAdminRights) listener.onMemberTapped.invoke(memberEntity) }
                .toAppCardStyle(overridePadding = 0.dp),
    ) {
        AsyncImageWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(image_size_large),
            profileImageUrl = memberEntity.profileImageUrl,
        )

        Column(
            modifier =
                Modifier
                    .padding(padding_screen_small),
        ) {
            TextWidget(
                text = memberEntity.getFullName(),
                style = AppTheme.textStyles.regularBold,
            )

            if (hasAdminRights) {
                TextWidget(
                    style = AppTheme.textStyles.small,
                    text = memberEntity.getResidentialStatus(),
                )

                if (memberEntity.hasPaidMembership()) {
                    TextWidget(
                        style = AppTheme.textStyles.small,
                        modifier = Modifier.fillMaxWidth(),
                        text = memberEntity.renewalFutureDateMillis.toMembershipStatusString(),
                    )
                    TextWidget(
                        style = AppTheme.textStyles.regularBold,
                        text = memberEntity.amountDue.toAmountString(),
                    )
                }
            }
        }

        if (hasAdminRights) {
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = padding_screen_small)
                        .padding(bottom = padding_screen_small)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    modifier =
                        Modifier
                            .iconModifier()
                            .clickable {
                                listener.onPaymentsTapped.invoke(memberEntity)
                            },
                    imageVector = Icons.Rounded.AttachMoney,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )
                Icon(
                    modifier =
                        Modifier
                            .padding(horizontal = padding_screen)
                            .iconModifier().clickable {
                                listener.onAttendanceTapped.invoke(memberEntity)
                            },
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )
                if (memberEntity.hasPaidMembership()) {
                    Icon(
                        modifier =
                            Modifier
                                .iconModifier()
                                .background(
                                    color = if (memberEntity.isActiveNow()) Color.Green else Color.Transparent,
                                    shape = RoundedCornerShape(border_radius),
                                )
                                .clickable {
                                    listener.onSessionTapped.invoke(memberEntity)
                                },
                        imageVector = Icons.AutoMirrored.Rounded.DirectionsRun,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun Modifier.iconModifier() =
    this
        .size(icon_size_small)
        .border(
            shape = RoundedCornerShape(border_radius),
            width = line_thickness,
            color = AppTheme.colors.primary,
        )

data class MemberStatusWidgetListener(
    val onMemberTapped: (memberEntity: MemberEntity) -> Unit = {},
    val onPaymentsTapped: (memberEntity: MemberEntity) -> Unit = {},
    val onAttendanceTapped: (memberEntity: MemberEntity) -> Unit = {},
    val onSessionTapped: (memberEntity: MemberEntity) -> Unit = {},
)

@AppPreview
@Composable
private fun MemberWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberStatusWidget(
                memberEntity = getMockRegisteredMemberEntity(),
                hasAdminRights = true,
            )
            MemberStatusWidget(
                memberEntity = getMockActiveMemberEntity(),
                hasAdminRights = true,
            )
            MemberStatusWidget(
                memberEntity = getMockExpiredMemberEntity(),
                hasAdminRights = true,
            )
            MemberStatusWidget(
                memberEntity = getMockActiveMemberEntity(),
            )
        }
    }
}

package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_size_small
import com.ndemi.garden.gym.ui.theme.image_size_small
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.AsyncImageWidget
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
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
                .padding(bottom = padding_screen_small)
                .toAppCardStyle(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { if (hasAdminRights) listener.onMemberTapped.invoke(memberEntity) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImageWidget(
                modifier =
                    Modifier
                        .width(image_size_small)
                        .height(image_size_small),
                profileImageUrl = memberEntity.profileImageUrl,
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = padding_screen_small),
            ) {
                TextWidget(
                    text = memberEntity.getFullName(),
                    style = AppTheme.textStyles.large,
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
                            style = AppTheme.textStyles.small,
                            text = memberEntity.amountDue.toAmountString(),
                        )
                    }
                }
            }

            if (hasAdminRights && memberEntity.hasCoach) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier =
                            Modifier
                                .padding(top = padding_screen_small)
                                .width(icon_size_small)
                                .height(icon_size_small),
                        imageVector = Icons.Rounded.Accessibility,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                    )
                    TextWidget(
                        text = stringResource(R.string.txt_coach),
                        color = AppTheme.colors.primary,
                        style = AppTheme.textStyles.small,
                    )
                }
            }
        }

        if (hasAdminRights) {
            Row(
                modifier =
                    Modifier
                        .padding(top = padding_screen_tiny)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ButtonWidget(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.txt_payments),
                    isOutlined = true,
                ) {
                    listener.onPaymentsTapped.invoke(memberEntity)
                }
                Spacer(modifier = Modifier.width(padding_screen_small))
                ButtonWidget(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.txt_attendance),
                    isOutlined = true,
                ) {
                    listener.onAttendanceTapped.invoke(memberEntity)
                }
                if (memberEntity.hasPaidMembership()) {
                    Spacer(modifier = Modifier.width(padding_screen_small))
                    ButtonWidget(
                        modifier = Modifier.weight(1f),
                        title =
                            if (memberEntity.isActiveNow()) {
                                stringResource(R.string.txt_end_session)
                            } else {
                                stringResource(R.string.txt_start_session)
                            },
                        isOutlined = !memberEntity.isActiveNow(),
                    ) {
                        listener.onSessionTapped.invoke(memberEntity)
                    }
                }
            }
        }
    }
}

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

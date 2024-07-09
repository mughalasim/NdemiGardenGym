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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material.icons.rounded.RunCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.ButtonOutlineWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextRegularBold
import com.ndemi.garden.gym.ui.widgets.TextSmall
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
@Composable
fun MemberStatusWidget(
    modifier: Modifier = Modifier,
    memberEntity: MemberEntity,
    showDetails: Boolean = false,
    onMemberTapped: (memberEntity: MemberEntity) -> Unit = {},
    onPaymentsTapped: (memberEntity: MemberEntity) -> Unit = {},
    onAttendanceTapped: (memberEntity: MemberEntity) -> Unit = {},
    onSessionTapped: (memberEntity: MemberEntity) -> Unit = {},
) {
    Column(
        modifier =
        modifier
            .padding(bottom = padding_screen_small)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = AppTheme.colors.backgroundCard,
                shape = RoundedCornerShape(border_radius)
            )
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundCardBorder,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen_small),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (showDetails) onMemberTapped.invoke(memberEntity) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageWidget(
                profileImageUrl = memberEntity.profileImageUrl,
                isLarge = false
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = padding_screen_small)
            ) {
                TextRegularBold(
                    text = memberEntity.getFullName(),
                )

                TextSmall(
                    text = DateTime.now().toActiveStatusDuration(
                        startDate = DateTime(memberEntity.activeNowDateMillis)
                    )
                )
                if (showDetails) {
                    TextSmall(
                        color = AppTheme.colors.backgroundError,
                        text = memberEntity.getResidentialStatus()
                    )
                }
            }

            if (showDetails) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (memberEntity.isActiveNow()) {
                        Icon(
                            modifier = Modifier
                                .padding(top = padding_screen_small)
                                .width(icon_image_size)
                                .height(icon_image_size),
                            imageVector = Icons.Rounded.RunCircle,
                            contentDescription = null,
                            tint = AppTheme.colors.highLight,
                        )
                    }

                    if (memberEntity.hasCoach) {
                        Icon(
                            modifier = Modifier
                                .padding(top = padding_screen_small)
                                .width(icon_image_size)
                                .height(icon_image_size),
                            imageVector = Icons.Rounded.Accessibility,
                            contentDescription = null,
                            tint = AppTheme.colors.highLight,
                        )
                        TextSmall(
                            text = stringResource(R.string.txt_coach),
                            color = AppTheme.colors.highLight
                        )
                    }
                }
            }
        }


        if (showDetails) {
            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(
                    color = AppTheme.colors.highLight,
                    text = stringResource(R.string.txt_membership_due_date)
                )
                TextRegular(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.renewalFutureDateMillis.toMembershipStatusString()
                )
            }

            if (memberEntity.hasPaidMembership()) {
                Row(
                    modifier = Modifier
                        .padding(top = padding_screen_tiny)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextSmall(
                        color = AppTheme.colors.highLight,
                        text = stringResource(R.string.txt_amount_due)
                    )
                    TextRegularBold(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.backgroundError,
                        text = memberEntity.amountDue.toAmountString()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonOutlineWidget(text = stringResource(R.string.txt_payments)) {
                    onPaymentsTapped.invoke(memberEntity)
                }
                ButtonOutlineWidget(text = stringResource(R.string.txt_attendance)) {
                    onAttendanceTapped.invoke(memberEntity)
                }
                ButtonOutlineWidget(
                    text = if (memberEntity.isActiveNow()) {
                        stringResource(R.string.txt_end_session)
                    } else {
                        stringResource(R.string.txt_start_session)
                    },
                    hasOutline = !memberEntity.isActiveNow(),
                    backgroundColor = if (memberEntity.isActiveNow()) {
                        AppTheme.colors.backgroundError
                    } else {
                        Color.Transparent
                    }
                ) {
                    onSessionTapped.invoke(memberEntity)
                }
            }
        }
    }
}

@AppPreview
@Composable
fun MemberWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberStatusWidget(
                memberEntity = getMockRegisteredMemberEntity(),
                showDetails = true
            )
            MemberStatusWidget(
                memberEntity = getMockActiveMemberEntity(),
                showDetails = true
            )
            MemberStatusWidget(
                memberEntity = getMockExpiredMemberEntity(),
                showDetails = true
            )
            MemberStatusWidget(
                memberEntity = getMockRegisteredMemberEntity(),
                showDetails = false
            )
        }
    }
}

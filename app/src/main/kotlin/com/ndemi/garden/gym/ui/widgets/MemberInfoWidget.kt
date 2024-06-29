package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.toDaysDuration
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberInfoWidget(
    memberEntity: MemberEntity,
    showExtraInfo: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundCardBorder,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = stringResource(R.string.txt_full_name)
            )
            TextRegularBold(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.getFullName()
            )
        }

        Row(
            modifier = Modifier
                .padding(top = padding_screen_tiny)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = stringResource(id = R.string.txt_email)
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.email
            )
        }

        if (showExtraInfo){
            Spacer(
                modifier = Modifier
                    .padding(top = padding_screen)
                    .fillMaxWidth()
                    .height(line_thickness)
                    .background(AppTheme.colors.highLight)
            )

            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(
                    color = AppTheme.colors.highLight,
                    text = stringResource(R.string.txt_residence)
                )
                TextRegular(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.getResidentialStatus()
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(
                    color = AppTheme.colors.highLight,
                    text = stringResource(R.string.txt_training_coach_assigned)
                )
                TextRegularBold(
                    color = if(memberEntity.hasCoach) AppTheme.colors.highLight else AppTheme.colors.backgroundError,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.getCoachStatus()
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(top = padding_screen)
                    .fillMaxWidth()
                    .height(line_thickness)
                    .background(AppTheme.colors.highLight)
            )

            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(
                    color = AppTheme.colors.highLight,
                    text = stringResource(R.string.txt_registration_date)
                )
                TextRegular(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = DateTime(memberEntity.registrationDateMillis).toString(
                        DateConstants.formatDayMonthYear
                    )
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = padding_screen_tiny)
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
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
                        .fillMaxWidth()
                        .padding(top = padding_screen_small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextSmall(
                        color = AppTheme.colors.highLight,
                        text = stringResource(R.string.txt_payment_due_in)
                    )
                    TextRegular(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = DateTime(memberEntity.renewalFutureDateMillis).toDaysDuration()
                    )
                }
            }
        }
    }
}

@AppPreview
@Composable
fun MemberInfoWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberInfoWidget(memberEntity = getMockRegisteredMemberEntity())
            MemberInfoWidget(memberEntity = getMockActiveMemberEntity())
            MemberInfoWidget(memberEntity = getMockExpiredMemberEntity())
        }
    }
}

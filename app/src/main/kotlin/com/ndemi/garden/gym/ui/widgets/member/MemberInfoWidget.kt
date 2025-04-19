package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberInfoWidget(
    memberEntity: MemberEntity,
    showExtraInfo: Boolean = true,
) {
    Column(
        modifier = Modifier.toAppCardStyle(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            TextWidget(
                color = AppTheme.colors.primary,
                text = stringResource(R.string.txt_full_name),
                style = AppTheme.textStyles.small,
            )
            TextWidget(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.getFullName(),
                style = AppTheme.textStyles.regularBold,
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            TextWidget(
                color = AppTheme.colors.primary,
                text = stringResource(id = R.string.txt_email),
                style = AppTheme.textStyles.small,
            )
            TextWidget(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.email,
            )
        }

        if (showExtraInfo) {
            Spacer(
                modifier =
                    Modifier
                        .padding(top = padding_screen_small)
                        .fillMaxWidth()
                        .height(line_thickness)
                        .background(AppTheme.colors.primary),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                TextWidget(
                    color = AppTheme.colors.primary,
                    text = stringResource(R.string.txt_residence),
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.getResidentialStatus(),
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                TextWidget(
                    color = AppTheme.colors.primary,
                    text = stringResource(R.string.txt_training_coach_assigned),
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    color = if (memberEntity.hasCoach) AppTheme.colors.primary else AppTheme.colors.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.getCoachStatus(),
                    style = AppTheme.textStyles.regularBold,
                )
            }

            Spacer(
                modifier =
                    Modifier
                        .padding(top = padding_screen_small)
                        .fillMaxWidth()
                        .height(line_thickness)
                        .background(AppTheme.colors.primary),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                TextWidget(
                    color = AppTheme.colors.primary,
                    text = stringResource(R.string.txt_registration_date),
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text =
                        DateTime(memberEntity.registrationDateMillis).toString(
                            DateConstants.formatDayMonthYear,
                        ),
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                TextWidget(
                    color = AppTheme.colors.primary,
                    text = stringResource(R.string.txt_membership_due_date),
                    style = AppTheme.textStyles.small,
                )
                TextWidget(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = memberEntity.renewalFutureDateMillis.toMembershipStatusString(),
                )
            }
            if (memberEntity.hasPaidMembership()) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = padding_screen_small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    TextWidget(
                        color = AppTheme.colors.primary,
                        text = "Amount due",
                        style = AppTheme.textStyles.small,
                    )
                    TextWidget(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.error,
                        text = memberEntity.amountDue.toAmountString(),
                        style = AppTheme.textStyles.regularBold,
                    )
                }
            }
        }
    }
}

@AppPreview
@Composable
private fun MemberInfoWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberInfoWidget(memberEntity = getMockRegisteredMemberEntity())
            MemberInfoWidget(memberEntity = getMockActiveMemberEntity())
            MemberInfoWidget(memberEntity = getMockExpiredMemberEntity())
        }
    }
}

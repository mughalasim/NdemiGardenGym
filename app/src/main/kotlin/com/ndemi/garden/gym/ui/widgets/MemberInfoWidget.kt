package com.ndemi.garden.gym.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.toDaysDuration
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.joda.time.DateTime

@Composable
fun MemberInfoWidget(
    memberEntity: MemberEntity = getMockMemberEntity()
) {
    Column(
        modifier = Modifier
            .padding(top = padding_screen)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness_small,
                color = AppTheme.colors.backgroundChip,
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
                text = "Name:"
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.getFullName()
            )
        }

        Row(
            modifier = Modifier.padding(top = padding_screen_tiny)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = "Email:"
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.email
            )
        }

        Row(
            modifier = Modifier.padding(top = padding_screen_tiny)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = "Residence:"
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.getResidentialStatus()
            )
        }

        Row(
            modifier = Modifier.padding(top = padding_screen_tiny)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = "Registration date:"
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = DateTime(memberEntity.registrationDate).toString(
                    DateConstants.formatDayMonthYear
                )
            )
        }

        Row(
            modifier = Modifier.padding(top = padding_screen_tiny)
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = "Membership due date:"
            )
            TextRegular(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = memberEntity.renewalFutureDate.toMembershipStatusString()
            )
        }
        if (memberEntity.hasPaidMembership()) {
            Row(
                modifier = Modifier.padding(top = padding_screen_tiny)
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(
                    color = AppTheme.colors.highLight,
                    text = "Payment due in:"
                )
                TextRegular(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = DateTime(memberEntity.renewalFutureDate).toDaysDuration()
                )
            }
        }
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MemberInfoWidgetPreviewNight() {
    AppThemeComposable {
        MemberInfoWidget()
    }
}

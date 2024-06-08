package com.ndemi.garden.gym.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.toHoursMinutesDuration
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.joda.time.DateTime

@Composable
fun MemberWidget(
    modifier: Modifier = Modifier,
    memberEntity: MemberEntity,
    showDetails: Boolean = false,
) {
    Column(
        modifier =
        modifier
            .padding(bottom = padding_screen_small)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness_small,
                color = AppTheme.colors.backgroundChip,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen_small),
    ) {
        TextRegular(
            text = memberEntity.firstName + " " + memberEntity.lastName,
        )
        if (memberEntity.isActiveNow()){
            TextRegular(
                text = "Session length: " + DateTime.now().toHoursMinutesDuration(
                    startDate = DateTime(memberEntity.activeNowDate)
                ),
            )
        }

        if (showDetails){
            Spacer(modifier = Modifier.padding(top = padding_screen_small))
            TextSmall(
                text =  "Email: " + memberEntity.email,
            )
            TextSmall(
                text =  "Member since: " + DateTime(memberEntity.registrationDate).toString(formatDayMonthYear),
            )
            Spacer(modifier = Modifier.padding(top = padding_screen_small))
            TextRegular(
                text =  "Membership renewal: " + memberEntity.renewalFutureDate.toMembershipStatusString(),
            )
        }
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MemberWidgetPreviewNight() {
    AppThemeComposable {
        MemberWidget(memberEntity = getMockMemberEntity())
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun MemberWidgetPreview() {
    AppThemeComposable {
        MemberWidget(memberEntity = getMockMemberEntity())
    }
}

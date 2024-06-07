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
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDateDay
import com.ndemi.garden.gym.ui.utils.DateConstants.formatTime
import com.ndemi.garden.gym.ui.utils.toHoursMinutesDuration
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.getMockAttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceWidget(
    modifier: Modifier = Modifier,
    attendanceEntity: AttendanceEntity,
) {
    val startDate = DateTime(attendanceEntity.startDate)
    val endDate = DateTime(attendanceEntity.endDate)
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
            ).padding(padding_screen_small),
    ) {
        TextSmall(
            text = startDate.toString(formatDateDay),
        )
        Row (
            modifier = Modifier
                .padding(top = padding_screen_small)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextRegular(
                text = startDate.toString(formatTime) + " - " + endDate.toString(formatTime),
            )

            TextRegular(
                text = endDate.toHoursMinutesDuration(startDate),
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
fun AttendanceWidgetPreviewNight() {
    AppThemeComposable {
        AttendanceWidget(attendanceEntity = getMockAttendanceEntity())
    }
}

@Preview(
    showBackground = false,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun AttendanceWidgetPreview() {
    AppThemeComposable {
        AttendanceWidget(attendanceEntity = getMockAttendanceEntity())
    }
}

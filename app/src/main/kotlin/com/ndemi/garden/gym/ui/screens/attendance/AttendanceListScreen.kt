package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockAttendanceEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.attendance.AttendanceWidget
import cv.domain.entities.AttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceListScreen(
    monthName: String,
    attendances: List<AttendanceEntity> = emptyList(),
    totalMinutes: Int = 0,
    canDeleteAttendance: Boolean = false,
    onDeleteAttendance: (AttendanceEntity) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = padding_screen)
    ) {
        TextWidget(
            text = monthName,
            style = AppTheme.textStyles.large,
        )

        // TODO - Move this to top level for yearly attendance instead of monthly
        if (attendances.isEmpty()) {
            TextWidget(
                text = stringResource(R.string.txt_no_attendances, monthName),
            )
        } else {
            TextWidget(
                modifier =
                    Modifier
                        .padding(top = padding_screen_small)
                        .fillMaxWidth(),
                text =
                    stringResource(
                        R.string.txt_total_time_spent,
                        DateTime.now().plusMinutes(totalMinutes).toActiveStatusDuration(
                            startDate = DateTime.now(),
                        ),
                    ),
            )
            repeat(attendances.size) {
                AttendanceWidget(
                    attendanceEntity = attendances[it],
                    canDeleteAttendance = canDeleteAttendance,
                    onDeleteAttendance = onDeleteAttendance,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = padding_screen)
                .fillMaxWidth()
                .height(line_thickness)
                .background(AppTheme.colors.backgroundButtonDisabled)
        )
    }
}

@AppPreview
@Composable
private fun AttendanceScreenPreview() {
    AppThemeComposable {
        Column {
            AttendanceListScreen(
                monthName = "November",
                attendances =
                    listOf(
                        getMockAttendanceEntity(),
                        getMockAttendanceEntity(),
                        getMockAttendanceEntity(),
                    ),
                totalMinutes = 20,
                canDeleteAttendance = false,
            )
            AttendanceListScreen(
                monthName = "December",
            )
        }
    }
}

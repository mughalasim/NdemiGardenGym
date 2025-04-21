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
import com.ndemi.garden.gym.ui.utils.toMonthName
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.attendance.AttendanceWidget
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import org.joda.time.DateTime

@Composable
fun AttendanceListScreen(
    attendanceMonthly: AttendanceMonthEntity,
    canDeleteAttendance: Boolean = false,
    onDeleteAttendance: (AttendanceEntity) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = padding_screen),
    ) {
        TextWidget(
            text = attendanceMonthly.monthNumber.toMonthName(),
            style = AppTheme.textStyles.large,
        )

        TextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen_small)
                    .fillMaxWidth(),
            text =
                stringResource(
                    R.string.txt_total_time_spent,
                    DateTime.now().plusMinutes(attendanceMonthly.totalMinutes).toActiveStatusDuration(
                        startDate = DateTime.now(),
                    ),
                ),
        )
        repeat(attendanceMonthly.attendances.size) {
            AttendanceWidget(
                attendanceEntity = attendanceMonthly.attendances[it],
                canDeleteAttendance = canDeleteAttendance,
                onDeleteAttendance = onDeleteAttendance,
            )
        }

        Spacer(
            modifier =
                Modifier
                    .padding(vertical = padding_screen)
                    .fillMaxWidth()
                    .height(line_thickness)
                    .background(AppTheme.colors.backgroundButtonDisabled),
        )
    }
}

@AppPreview
@Composable
private fun AttendanceScreenPreview() {
    AppThemeComposable {
        Column {
            AttendanceListScreen(
                attendanceMonthly =
                    AttendanceMonthEntity(
                        monthNumber = 1,
                        totalMinutes = 45,
                        attendances = listOf(getMockAttendanceEntity()),
                    ),
                canDeleteAttendance = false,
            )
            AttendanceListScreen(
                attendanceMonthly = AttendanceMonthEntity(),
            )
        }
    }
}

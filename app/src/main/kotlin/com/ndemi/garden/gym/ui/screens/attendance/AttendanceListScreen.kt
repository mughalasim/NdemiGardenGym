package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockAttendanceEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.attendance.AttendanceWidget
import cv.domain.entities.AttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceListScreen(
    attendances: List<AttendanceEntity>,
    totalMinutes: Int,
    canDeleteAttendance: Boolean,
    onDeleteAttendance: (AttendanceEntity) -> Unit = {},
) {
    Column {
        Row {
            TextRegular(
                modifier = Modifier
                    .padding(horizontal = padding_screen)
                    .padding(top = padding_screen_small)
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.txt_total_time_spent,
                    DateTime.now().plusMinutes(totalMinutes).toActiveStatusDuration(
                        startDate = DateTime.now()
                    )
                ),
                textAlign = TextAlign.End
            )
        }
        repeat(attendances.size) {
            AttendanceWidget(
                attendanceEntity = attendances[it],
                canDeleteAttendance = canDeleteAttendance,
                onDeleteAttendance = onDeleteAttendance
            )
        }
    }
}

@AppPreview
@Composable
fun AttendanceScreenPreview() {
    AppThemeComposable {
        AttendanceListScreen(
            attendances = listOf(
                getMockAttendanceEntity(),
                getMockAttendanceEntity(),
                getMockAttendanceEntity(),
            ),
            totalMinutes = 20,
            canDeleteAttendance = false
        )
    }
}

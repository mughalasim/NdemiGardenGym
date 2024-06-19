package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.attendanceWidget
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.getMockAttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceListScreen(
    attendances: List<AttendanceEntity>,
    canDeleteAttendance: Boolean,
    onDeleteAttendance: (AttendanceEntity) -> Unit = {},
) {
    Column {
        var totalMinutes  = 0
        repeat(attendances.size) {
            totalMinutes += attendanceWidget(
                attendanceEntity = attendances[it],
                canDeleteAttendance = canDeleteAttendance,
                onDeleteAttendance = onDeleteAttendance
            )
        }
        Row {
            TextRegular(
                modifier = Modifier
                    .padding(vertical = padding_screen)
                    .fillMaxWidth(),
                text = "Total time spent: ${
                    DateTime.now().plusMinutes(totalMinutes).toActiveStatusDuration(
                        startDate = DateTime.now()
                    )
                }",
                textAlign = TextAlign.End
            )
        }
    }
}

@AppPreview
@Composable
@Suppress("detekt.MagicNumber")
fun AttendanceScreenPreview() {
    AppThemeComposable {
        AttendanceListScreen(
            attendances = listOf(
                getMockAttendanceEntity(
                    startDate = DateTime.now().plusDays(1).plusHours(2).plusMinutes(3).toDate(),
                    endDate = DateTime.now().plusDays(1).plusHours(3).plusMinutes(3).toDate()
                ),
                getMockAttendanceEntity(
                    startDate = DateTime.now().plusDays(2).plusHours(2).plusMinutes(2).toDate(),
                    endDate = DateTime.now().plusDays(2).plusHours(3).plusMinutes(43).toDate()
                ),
                getMockAttendanceEntity(
                    startDate = DateTime.now().plusDays(3).plusHours(2).plusMinutes(3).toDate(),
                    endDate = DateTime.now().plusDays(3).plusHours(3).toDate()

                ),
            ),
            canDeleteAttendance = false
        )
    }
}

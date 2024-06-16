package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AttendanceWidget
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.getMockAttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceItemsScreen(
    attendances: List<AttendanceEntity>,
    onDeleteAttendance: (AttendanceEntity)->Unit = {},
) {
    Column {
        repeat(attendances.size) {
            AttendanceWidget(
                attendanceEntity = attendances[it],
                onDeleteAttendance = onDeleteAttendance
            )
        }
    }
}

@AppPreview
@Composable
@Suppress("detekt.MagicNumber")
fun AttendanceScreenPreview() {
    AppThemeComposable {
        AttendanceItemsScreen(
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
        )
    }
}

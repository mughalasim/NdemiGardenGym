package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockAttendancePresentationModel
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.icon_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.attendance.AttendanceWidget
import cv.domain.presentationModels.AttendanceMonthPresentationModel
import cv.domain.presentationModels.AttendancePresentationModel

@Composable
fun AttendanceListScreen(
    attendanceMonthly: AttendanceMonthPresentationModel,
    canDeleteAttendance: Boolean = false,
    onDeleteAttendance: (AttendancePresentationModel) -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(top = padding_screen_small)
                .padding(horizontal = padding_screen)
                .toAppCardStyle(),
    ) {
        var collapsedState: Boolean by remember { mutableStateOf(true) }
        Row {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextWidget(
                    text = attendanceMonthly.monthName,
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
                            attendanceMonthly.activeDuration,
                        ),
                )
            }
            IconButton(
                modifier = Modifier.size(icon_size_large),
                onClick = { collapsedState = !collapsedState },
            ) {
                Icon(
                    modifier = Modifier.size(icon_size_large),
                    tint = AppTheme.colors.primary,
                    imageVector =
                        if (!collapsedState) {
                            Icons.Default.ArrowDropDown
                        } else {
                            Icons.Default.ArrowDropUp
                        },
                    contentDescription = null,
                )
            }
        }
        if (collapsedState) {
            repeat(attendanceMonthly.attendances.size) {
                AttendanceWidget(
                    model = attendanceMonthly.attendances[it],
                    canDeleteAttendance = canDeleteAttendance,
                    onDeleteAttendance = onDeleteAttendance,
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun AttendanceScreenPreview() {
    AppThemeComposable {
        Column {
            AttendanceListScreen(
                attendanceMonthly =
                    AttendanceMonthPresentationModel(
                        monthNumber = 1,
                        totalMinutes = 45,
                        monthName = "September",
                        activeDuration = "12 minutes",
                        attendances =
                            listOf(
                                getMockAttendancePresentationModel(),
                                getMockAttendancePresentationModel(),
                                getMockAttendancePresentationModel(),
                                getMockAttendancePresentationModel(),
                            ),
                    ),
                canDeleteAttendance = true,
            )
            AttendanceListScreen(
                attendanceMonthly =
                    AttendanceMonthPresentationModel(
                        monthName = "October",
                        activeDuration = "12 minutes",
                    ),
            )
        }
    }
}

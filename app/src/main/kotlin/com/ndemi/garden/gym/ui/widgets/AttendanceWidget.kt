package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDateDay
import com.ndemi.garden.gym.ui.utils.DateConstants.formatTime
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.getMockAttendanceEntity
import org.joda.time.DateTime
import org.joda.time.Minutes

@Composable
fun attendanceWidget(
    modifier: Modifier = Modifier,
    attendanceEntity: AttendanceEntity,
    canDeleteAttendance: Boolean = false,
    onDeleteAttendance: (AttendanceEntity)-> Unit = {},
): Int {
    val startDate = DateTime(attendanceEntity.startDate)
    val endDate = DateTime(attendanceEntity.endDate)
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
        modifier
            .padding(horizontal = padding_screen)
            .padding(top = padding_screen_small)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundChip,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen_small),
    ) {
        Row(
            modifier = Modifier
                .padding(top = padding_screen_small)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextSmall(
                text = startDate.toString(formatDateDay),
            )
            if (canDeleteAttendance) {
                Icon(
                    modifier = Modifier.clickable { showDialog = !showDialog },
                    imageVector = Icons.Default.Clear,
                    tint = AppTheme.colors.highLight,
                    contentDescription = "Delete"
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = padding_screen_small)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextRegular(
                text = startDate.toString(formatTime) + " - " + endDate.toString(formatTime),
            )

            TextRegular(
                text = endDate.toActiveStatusDuration(startDate),
            )

            if (showDialog){
                AlertDialog(
                    containerColor = AppTheme.colors.backgroundButtonDisabled,
                    title = { TextSmall(text = "Are you sure") },
                    text = {
                        TextRegular(
                            text = "Are you sure you wish to delete this Attendance, This action is permanent"
                        )
                    },
                    onDismissRequest = { showDialog = !showDialog },
                    confirmButton = {
                        ButtonWidget(title = "Delete") {
                            showDialog = !showDialog
                            onDeleteAttendance.invoke(attendanceEntity)
                        }
                    },
                    dismissButton = {
                        ButtonWidget(title = "Cancel") {
                            showDialog = !showDialog
                        }
                    })
            }
        }
    }
    return Minutes.minutesBetween(
        startDate.toInstant(),
        endDate.toInstant()
    ).minutes
}

@AppPreview
@Composable
fun AttendanceWidgetPreview() {
    AppThemeComposable {
        attendanceWidget(attendanceEntity = getMockAttendanceEntity())
    }
}

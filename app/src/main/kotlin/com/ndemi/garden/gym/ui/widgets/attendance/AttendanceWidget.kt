package com.ndemi.garden.gym.ui.widgets.attendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockAttendanceEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDateDay
import com.ndemi.garden.gym.ui.utils.DateConstants.formatTime
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import cv.domain.entities.AttendanceEntity
import org.joda.time.DateTime

@Composable
fun AttendanceWidget(
    modifier: Modifier = Modifier,
    attendanceEntity: AttendanceEntity,
    canDeleteAttendance: Boolean = false,
    onDeleteAttendance: (AttendanceEntity) -> Unit = {},
) {
    val startDate = DateTime(attendanceEntity.startDateMillis)
    val endDate = DateTime(attendanceEntity.endDateMillis)
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .padding(top = padding_screen_small),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextWidget(
                text = startDate.toString(formatDateDay),
                style = AppTheme.textStyles.small,
                color = AppTheme.colors.primary,
            )
            if (canDeleteAttendance) {
                Icon(
                    modifier = Modifier.clickable { showDialog = !showDialog },
                    imageVector = Icons.Default.DeleteForever,
                    tint = AppTheme.colors.error,
                    contentDescription = stringResource(id = R.string.txt_delete),
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .padding(top = padding_screen_small)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextWidget(
                text =
                    startDate.toString(formatTime) +
                        " - " +
                        endDate.toString(formatTime),
            )

            TextWidget(
                text = endDate.toActiveStatusDuration(startDate),
            )

            if (showDialog) {
                AlertDialogWidget(
                    title = stringResource(R.string.txt_are_you_sure),
                    message = stringResource(R.string.txt_are_you_sure_delete_attendance),
                    onDismissed = { showDialog = !showDialog },
                    positiveButton = stringResource(R.string.txt_delete),
                    positiveOnClick = {
                        showDialog = !showDialog
                        onDeleteAttendance.invoke(attendanceEntity)
                    },
                    negativeButton = stringResource(R.string.txt_cancel),
                    negativeOnClick = {
                        showDialog = !showDialog
                    },
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun AttendanceWidgetPreview() =
    AppThemeComposable {
        Column {
            AttendanceWidget(attendanceEntity = getMockAttendanceEntity())
            AttendanceWidget(attendanceEntity = getMockAttendanceEntity(), canDeleteAttendance = true)
        }
    }

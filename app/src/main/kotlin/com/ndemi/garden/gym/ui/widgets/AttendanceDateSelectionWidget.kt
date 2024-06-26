package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatMonthYear
import org.joda.time.DateTime

@Composable
fun AttendanceDateSelectionWidget(
    selectedDate: DateTime,
    onDateSelected: (selectedDate: DateTime) -> Unit = {}
) {
    var monthPickerVisibility by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(start = padding_screen, end = padding_screen, top = padding_screen)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundChip,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen),
    ) {
        TextRegular(text = "Select a date range")

        ButtonWidget(
            title = selectedDate.toString(formatMonthYear),
        ) {
            monthPickerVisibility = !monthPickerVisibility
        }
    }

    MonthPicker(
        visible = monthPickerVisibility,
        currentMonth = selectedDate.monthOfYear - 1,
        currentYear = selectedDate.year,
        confirmButtonCLicked = { month, year ->
            monthPickerVisibility = !monthPickerVisibility
            onDateSelected.invoke(DateTime.now().withDate(year, month, 1))
        }
    ) {
        monthPickerVisibility = !monthPickerVisibility
    }
}

@AppPreview
@Composable
fun AttendanceDateSelectionWidgetPreview(){
    AppThemeComposable {
        AttendanceDateSelectionWidget(DateTime.now()){}
    }
}

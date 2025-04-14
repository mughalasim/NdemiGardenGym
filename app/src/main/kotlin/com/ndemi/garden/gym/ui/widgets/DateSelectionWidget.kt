package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatMonthYear
import com.ndemi.garden.gym.ui.utils.DateConstants.formatYear
import org.joda.time.DateTime

@Composable
fun DateSelectionWidget(
    selectedDate: DateTime,
    hideMonthSelection: Boolean,
    onDateSelected: (selectedDate: DateTime) -> Unit,
) {
    var monthPickerVisibility by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(horizontal = padding_screen)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = padding_screen_small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextWidget(
            text = if (hideMonthSelection) {
                stringResource(R.string.txt_select_year)
            } else {
                stringResource(R.string.txt_select_date)
            }
        )

        ButtonOutlineWidget(
            text = if (hideMonthSelection) {
                selectedDate.toString(formatYear)
            } else {
                selectedDate.toString(formatMonthYear)
            }
        ) {
            monthPickerVisibility = !monthPickerVisibility
        }
    }

    MonthPickerWidget(
        visible = monthPickerVisibility,
        currentMonth = selectedDate.monthOfYear - 1,
        currentYear = selectedDate.year,
        hideMonthSelection = hideMonthSelection,
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
private fun AttendanceDateSelectionWidgetPreview() {
    AppThemeComposable {
        DateSelectionWidget(
            selectedDate = DateTime.now(),
            hideMonthSelection = true
        ) {}
    }
}

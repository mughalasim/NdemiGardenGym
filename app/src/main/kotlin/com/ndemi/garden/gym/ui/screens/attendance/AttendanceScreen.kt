package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.DateConstants.formatMonthYear
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MonthPicker
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceScreenViewModel = koinViewModel<AttendanceScreenViewModel>()
) {
    var selectedDate by remember { mutableStateOf(DateTime.now()) }
    var monthPickerVisibility by remember { mutableStateOf(false) }
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) { viewModel.getAttendances(selectedDate) }

    Column {
        ToolBarWidget(title = "Your Attendances")

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = padding_screen, end = padding_screen),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextLarge(
                modifier = Modifier.weight(1f).padding(top = padding_screen),
                text = selectedDate.toString(formatMonthYear))
            ButtonWidget(
                modifier = Modifier.weight(1f),
                title = "Select Date",
                isEnabled = true
            ) {
                monthPickerVisibility = !monthPickerVisibility
            }
        }

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getAttendances(selectedDate) }
        ) {
            if (uiState.value is UiState.Success) {
                if ((uiState.value as UiState.Success).attendances.isEmpty()) {
                    WarningWidget(title = "No Attendances for the selected month")
                } else {
                    AttendanceListScreen(
                        attendances = (uiState.value as UiState.Success).attendances,
                        canDeleteAttendance = false
                    ){
                        viewModel.deleteAttendance(it)
                    }
                }
            }
        }
    }

    MonthPicker(
        visible = monthPickerVisibility,
        currentMonth = selectedDate.monthOfYear - 1,
        currentYear = selectedDate.year,
        confirmButtonCLicked = { month, year ->
            monthPickerVisibility = !monthPickerVisibility
            selectedDate = DateTime.now().withDate(year, month, 1)
            viewModel.getAttendances(selectedDate)
        }
    ) {
        monthPickerVisibility = !monthPickerVisibility
    }
}

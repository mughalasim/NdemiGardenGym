package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.formatMonthYear
import com.ndemi.garden.gym.ui.widgets.AttendanceWidget
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MonthPicker
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.getMockAttendanceEntity
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceScreenViewModel = koinViewModel<AttendanceScreenViewModel>()
) {
    var selectedDate by remember { mutableStateOf(DateTime.now()) }
    var monthPickerVisibility by remember { mutableStateOf(false) }
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Loading
    )
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getAttendances(selectedDate)
    })

    LaunchedEffect(true) {
        viewModel.getAttendances(selectedDate)
    }

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(padding_screen),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextLarge(text = "Your attendance")

            Spacer(modifier = Modifier.padding(padding_screen_small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextLarge(text = selectedDate.toString(formatMonthYear))
                ButtonWidget(title = "Select Date", isEnabled = true) {
                    monthPickerVisibility = !monthPickerVisibility
                }
            }
            when (val state = uiState.value) {
                is UiState.Error -> WarningWidget(title = state.message)
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (state.attendances.isEmpty()) {
                        Spacer(modifier = Modifier.padding(padding_screen_small))
                        TextRegular(text = "No Attendances for the selected month")
                    } else {
                        AttendanceItemsScreen(attendances = state.attendances,)
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.padding(top = padding_screen),
                refreshing = isRefreshing,
                state = pullRefreshState,
            )

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
}

@Composable
fun AttendanceItemsScreen(
    attendances: List<AttendanceEntity>,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(padding_screen_small))
        repeat(attendances.size) {
            AttendanceWidget(attendanceEntity = attendances[it])
        }
    }
}

@Preview
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
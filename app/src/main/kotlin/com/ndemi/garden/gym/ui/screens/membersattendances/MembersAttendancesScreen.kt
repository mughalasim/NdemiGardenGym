package com.ndemi.garden.gym.ui.screens.membersattendances

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceListScreen
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersAttendancesScreen (
    memberId: String,
    memberName: String,
    viewModel: MembersAttendancesScreenViewModel = koinViewModel<MembersAttendancesScreenViewModel>()
) {
    var selectedDate by remember { mutableStateOf(DateTime.now()) }
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) { viewModel.getAttendances(memberId, selectedDate) }

    Column {
        ToolBarWidget(title = stringResource(R.string.txt_attendance_for, memberName), canNavigateBack = true){
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        DateSelectionWidget(selectedDate, false){
            selectedDate = it
            viewModel.getAttendances(memberId, selectedDate)
        }

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getAttendances(memberId, selectedDate) }
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (uiState.value is UiState.Success) {
                    val result = (uiState.value as UiState.Success)
                    if (result.attendances.isEmpty()) {
                        TextRegular(
                            modifier = Modifier.padding(padding_screen),
                            text = stringResource(R.string.txt_no_attendances)
                        )
                    } else {
                        AttendanceListScreen(
                            attendances = result.attendances,
                            canDeleteAttendance = true,
                            totalMinutes = result.totalMinutes
                        ){
                            viewModel.deleteAttendance(it)
                        }
                    }
                }
            }
        }
    }
}

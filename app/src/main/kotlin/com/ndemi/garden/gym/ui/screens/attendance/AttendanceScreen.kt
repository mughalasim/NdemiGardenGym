package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun AttendanceScreen(
    memberId: String = "",
    memberName: String = "",
    viewModel: AttendanceScreenViewModel = koinViewModel<AttendanceScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val permissionState by viewModel.getPermissions().collectAsStateWithLifecycle()

    val title =
        if (memberName.isEmpty()) {
            stringResource(R.string.txt_your_attendances)
        } else {
            stringResource(R.string.txt_attendance_for, memberName)
        }

    viewModel.setMemberId(memberId)

    LaunchedEffect(Unit) { viewModel.getAttendances() }

    Column {
        ToolBarWidget(
            title = title,
            canNavigateBack = memberName.isNotEmpty(),
        ) {
            viewModel.navigateBack()
        }

        DateSelectionWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding_screen),
            selectedText = selectedYear.toString(),
            isLoading = uiState is UiState.Loading,
            onPlusTapped = viewModel::increaseYear,
            onMinusTapped = viewModel::decreaseYear,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (val state = uiState) {
                is UiState.Success -> {
                    if (state.attendancesMonthly.isEmpty()) {
                        TextWidget(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(padding_screen),
                            text = stringResource(R.string.txt_no_attendances),
                            textAlign = TextAlign.Center,
                        )
                    }
                    for (attendanceMonthly in state.attendancesMonthly) {
                        AttendanceListScreen(
                            attendanceMonthly = attendanceMonthly,
                            canDeleteAttendance = permissionState.canDeleteAttendance,
                        ) {
                            viewModel.deleteAttendance(it)
                        }
                    }
                }

                is UiState.Error -> {
                    snackbarHostState.Show(
                        type = SnackbarType.ERROR,
                        message = state.message,
                    )
                }

                else -> {}
            }
        }
    }
}

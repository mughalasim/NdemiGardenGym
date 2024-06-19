package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreen(
    memberId: String,
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val sessionStartTime = viewModel.sessionStartTime.observeAsState().value

    LaunchedEffect(true) { viewModel.getMemberForId(memberId) }

    Column {
        ToolBarWidget(title = "Edit Member", canNavigateBack = true) {
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMemberForId(memberId) }
        ) {
            if (uiState.value is UiState.Success) {
                MemberEditDetailsScreen(
                    memberEntity = (uiState.value as UiState.Success).memberEntity,
                    onMembershipDueDateUpdate = { dateTime ->
                        viewModel.updateMember(dateTime)
                    },
                    onViewAttendance = {
                        viewModel.navigateToAttendanceScreen(it)
                    },
                    sessionMessage = (uiState.value as UiState.Success).message,
                    sessionStartTime = sessionStartTime,
                    onSessionStarted = viewModel::setStartedSession,
                    onSessionCompleted = viewModel::setAttendance
                )
            }
        }
    }
}

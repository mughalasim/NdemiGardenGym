package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemberEditScreen(
    memberId: String,
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMemberForId(memberId)
    })
    val sessionStartTime = viewModel.sessionStartTime.observeAsState().value

    LaunchedEffect(true) { viewModel.getMemberForId(memberId) }

    Column {
        ToolBarWidget(title = "Edit Member", canNavigateBack = true) {
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
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
            PullRefreshIndicator(
                backgroundColor = AppTheme.colors.highLight,
                refreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )
        }
    }
}

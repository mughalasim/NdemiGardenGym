package com.ndemi.garden.gym.ui.screens.profile

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
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = koinViewModel<ProfileScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMember()
    })
    val sessionStartTime = viewModel.sessionStartTime.observeAsState().value

    LaunchedEffect(true) { viewModel.getMember() }

    Column {
        ToolBarWidget(title = if(viewModel.isAdmin()) "Admin Profile" else "My profile")

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.value is UiState.Success) {
                ProfileDetailsScreen(
                    memberEntity = (uiState.value as UiState.Success).memberEntity,
                    isAdmin = viewModel.isAdmin(),
                    message = (uiState.value as UiState.Success).message,
                    sessionStartTime = sessionStartTime,
                    onSessionStarted = viewModel::setStartedSession,
                    onSessionCompleted = { startDate, endDate ->
                        viewModel.setAttendance(startDate, endDate)
                    },
                    onRegisterMember = viewModel::onRegisterMember,
                    onLogOut = viewModel::onLogOutTapped
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


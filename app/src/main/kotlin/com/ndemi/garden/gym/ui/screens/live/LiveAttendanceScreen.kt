package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveAttendanceScreen(
    viewModel: LiveAttendanceScreenViewModel = koinViewModel<LiveAttendanceScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getLiveMembers()
    })

    LaunchedEffect(true) { viewModel.getLiveMembers() }

    Column {
        ToolBarWidget(title = "Who's in the Gym today?")

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_screen)
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.value is UiState.Success) {
                if ((uiState.value as UiState.Success).members.isEmpty()) {
                    WarningWidget(title = "Oh no! No one's in, why don't you be the first")
                } else {
                    LiveAttendanceListScreen(members = (uiState.value as UiState.Success).members)
                }
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

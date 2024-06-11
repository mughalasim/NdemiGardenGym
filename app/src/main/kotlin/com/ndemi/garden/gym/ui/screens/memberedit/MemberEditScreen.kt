package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.screens.profile.ProfileListScreen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemberEditScreen(
    memberId: String,
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>()
) {

    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Loading
    )
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMemberForId(memberId)
    })

    LaunchedEffect(true) {
        viewModel.getMemberForId(memberId)
    }

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        if (uiState.value is UiState.Error) {
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
            Spacer(modifier = Modifier.padding(padding_screen_small))
        }

        when (val state = uiState.value) {
            is UiState.Success ->
                ProfileListScreen(
                    memberEntity = state.memberEntity,
                    message = state.message,
                    sessionStartTime = DateTime.now(),
                    onSessionStarted = {}
                    ,
                    onSessionCompleted = { startDate, endDate ->
                        viewModel.setAttendance(startDate, endDate)
                    },
                    onLogOut = {}
                )

            else -> Unit
        }
    }
}
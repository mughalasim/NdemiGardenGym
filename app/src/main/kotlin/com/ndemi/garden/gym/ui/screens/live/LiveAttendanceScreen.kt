package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.MemberWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveAttendanceScreen (
    viewModel: LiveAttendanceScreenViewModel = koinViewModel<LiveAttendanceScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Loading
    )
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMembers()
    })

    LaunchedEffect(true) {
        viewModel.getMembers()
    }

    Box (modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .padding(padding_screen),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextLarge(text = "Who is in the Gym today?")

            Spacer(modifier = Modifier.padding(padding_screen_small))

            when (val state = uiState.value){
                is UiState.Error -> WarningWidget(title = state.message)
                is UiState.Loading -> Unit
                is UiState.Success ->
                    if (state.members.isEmpty()){
                        Spacer(modifier = Modifier.padding(padding_screen_small))
                        TextRegular(text = "Oh no! No ones in, why don't you be the first")
                    } else {
                        LiveAttendanceListScreen(members = state.members)
                    }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
            )
        }
    }
}

@Composable
fun LiveAttendanceListScreen(members: List<MemberEntity>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(padding_screen_small))
        repeat(members.size) {
            MemberWidget(memberEntity = members[it])
        }
    }
}

@Preview
@Composable
fun LiveAttendanceListScreenPreview(){
    AppThemeComposable {
       LiveAttendanceListScreen(members = listOf(
           getMockMemberEntity(),
           getMockMemberEntity(),
           getMockMemberEntity()
       ))
    }
}
package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.formatTime
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.joda.time.DateTime
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
        ToolBarWidget(title = "Profile")

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.value is UiState.Success) {
                ProfileListScreen(
                    memberEntity = (uiState.value as UiState.Success).memberEntity,
                    message = (uiState.value as UiState.Success).message,
                    sessionStartTime = sessionStartTime,
                    onSessionStarted = viewModel::setStartedSession,
                    onSessionCompleted = { startDate, endDate ->
                        viewModel.setAttendance(startDate, endDate)
                    },
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

@Composable
fun ProfileListScreen(
    memberEntity: MemberEntity,
    message: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _,_ -> },
    onLogOut: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MemberInfoWidget(memberEntity)

        if (memberEntity.hasPaidMembership()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen)
                    .border(
                        width = line_thickness_small,
                        color = AppTheme.colors.backgroundChip,
                        shape = RoundedCornerShape(border_radius),
                    )
                    .padding(padding_screen_small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextLarge(text = "Workout session")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                ButtonWidget(
                    title = if (sessionStartTime != null) "End session" else "Start session",
                    isEnabled = true
                ) {
                    if (sessionStartTime != null){
                        onSessionCompleted.invoke(sessionStartTime, DateTime.now())
                    } else {
                        onSessionStarted.invoke()
                    }
                }

                if (sessionStartTime == null && message.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(padding_screen_small))
                    TextRegular(
                        text = message,
                        color = AppTheme.colors.backgroundError
                    )
                }

                if (sessionStartTime != null) {
                    Spacer(modifier = Modifier.padding(padding_screen_small))
                    TextSmall(text = "Your work out session is in progress...")
                    TextRegular(
                        text =
                        "Started at ${sessionStartTime.toString(formatTime)}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(title = "LogOut", isEnabled = sessionStartTime == null) {
            onLogOut.invoke()
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppThemeComposable {
        ProfileListScreen(
            memberEntity = getMockMemberEntity(),
        )
    }
}

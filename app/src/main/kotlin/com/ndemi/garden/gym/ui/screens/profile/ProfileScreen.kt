package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.DateConstants.formatTime
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
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
    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Loading
    )
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMember()
    })
    val sessionStartTime = viewModel.sessionStartTime.observeAsState().value

    LaunchedEffect(true) {
        viewModel.getMember()
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
                    sessionStartTime = sessionStartTime,
                    onSessionStarted = viewModel::setStartedSession
                    ,
                    onSessionCompleted = { startDate, endDate ->
                        viewModel.setAttendance(startDate, endDate)
                    },
                    onLogOut = viewModel::onLogOutTapped
                )

            else -> Unit
        }
    }
}

@Composable
fun ProfileListScreen(
    memberEntity: MemberEntity,
    message: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit,
    onLogOut: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextLarge(text = "Profile")

        Column(
            modifier = Modifier
                .padding(top = padding_screen)
                .fillMaxWidth()
                .border(
                    width = line_thickness_small,
                    color = AppTheme.colors.backgroundChip,
                    shape = RoundedCornerShape(border_radius),
                )
                .padding(padding_screen_small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(text = "First name:")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                TextRegular(text = memberEntity.firstName)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(text = "Last name:")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                TextRegular(text = memberEntity.lastName)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(text = "Email:")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                TextRegular(text = memberEntity.email)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(text = "Registration Date:")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                TextRegular(
                    text = DateTime(memberEntity.registrationDate).toString(
                        formatDayMonthYear
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen_small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextSmall(text = "Membership renewal Date:")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                TextRegular(text = memberEntity.renewalFutureDate.toMembershipStatusString())
            }

        }

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
//        ButtonWidget(title = "LogOut", isEnabled = sessionStartTime == null) {
//            onLogOut.invoke()
//        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppThemeComposable {
        ProfileListScreen(
            getMockMemberEntity(),
            "",
            null,
            {},
            { _, _ -> },
            {}
        )
    }
}

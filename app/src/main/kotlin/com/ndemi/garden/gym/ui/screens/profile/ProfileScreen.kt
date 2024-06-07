package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.getPaidStatusString
import com.ndemi.garden.gym.ui.widgets.AttendanceWidget
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.AttendanceEntity
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

    LaunchedEffect(true) {
        viewModel.getMember()
    }

    Box (modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize()
    ){
        when (val state = uiState.value){
            is UiState.Error -> WarningWidget(title = state.message)
            is UiState.Loading -> Unit
            is UiState.Success ->
                ProfileListScreen(memberEntity = state.memberEntity){
                    viewModel.onLogOutTapped()
                }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
fun ProfileListScreen(
    memberEntity: MemberEntity,
    logOutCallBack: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextLarge(text = "Profile")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
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
            TextRegular(text = DateTime(memberEntity.registrationDate).toString(formatDayMonthYear))
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
            TextRegular(text = memberEntity.renewalFutureDate.getPaidStatusString())
        }

        var sessionStarted by remember { mutableStateOf(true) }
        var sessionStartTime by remember { mutableStateOf(DateTime.now()) }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(title = if(sessionStarted) "End Session" else "Start Session", isEnabled = true) {
            sessionStarted = !sessionStarted
            sessionStartTime = DateTime.now()
        }

        if (sessionStarted){
            Spacer(modifier = Modifier.padding(padding_screen_small))
            AttendanceWidget(attendanceEntity = AttendanceEntity(
                memberId = "",
                startDate = sessionStartTime.toDate(),
                endDate = sessionStartTime.toDate()
            ))
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(title = "LogOut", isEnabled = true) {
            logOutCallBack.invoke()
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppThemeComposable {
        ProfileListScreen(getMockMemberEntity()){}
    }
}

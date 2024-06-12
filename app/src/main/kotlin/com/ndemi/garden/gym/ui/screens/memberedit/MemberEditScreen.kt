package com.ndemi.garden.gym.ui.screens.memberedit

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
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
fun MemberEditScreen(
    memberId: String,
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val isRefreshing = (uiState.value is UiState.Loading)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.getMemberForId(memberId)
    })

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
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditDetailsScreen(
    memberEntity: MemberEntity,
    onMembershipDueDateUpdate: (DateTime) -> Unit = { },
    onViewAttendance: (memberEntity: MemberEntity) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        var datePickerVisibility by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

        MemberInfoWidget(memberEntity)

        Column(
            modifier = Modifier
                .padding(top = padding_screen)
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    width = line_thickness_small,
                    color = AppTheme.colors.backgroundChip,
                    shape = RoundedCornerShape(border_radius),
                )
                .padding(padding_screen),
            horizontalAlignment = Alignment.Start
        ) {
            TextSmall(
                color = AppTheme.colors.highLight,
                text = "Update Membership Due date"
            )
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Please select the date the members subscription will end"
            )
            OutlinedButton(
                modifier = Modifier
                    .padding(top = padding_screen_small)
                    .fillMaxWidth()
                    .padding(end = padding_screen),
                onClick = {
                    datePickerVisibility = !datePickerVisibility
                    errorMessage = ""
                },
                shape = RoundedCornerShape(border_radius),
                border = BorderStroke(1.dp, color = AppTheme.colors.highLight),
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
            ) {
                TextRegular(text = memberEntity.renewalFutureDate.toMembershipStatusString(),)
            }
            if(errorMessage.isNotEmpty()){
                Spacer(modifier = Modifier.padding(top = padding_screen_small))
                WarningWidget(title = errorMessage)
            }

            if (datePickerVisibility){
                Spacer(modifier = Modifier.padding(top = padding_screen_small))
                DatePicker(state = state, showModeToggle = false, headline = null, title = null)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        modifier = Modifier.padding(end = padding_screen),
                        onClick = {
                            datePickerVisibility = !datePickerVisibility
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        TextRegular(text = "Cancel",)
                        errorMessage = ""
                    }

                    OutlinedButton(
                        modifier = Modifier.padding(end = padding_screen),
                        onClick = {
                            datePickerVisibility = !datePickerVisibility
                            errorMessage = ""
                            state.selectedDateMillis?.let {
                                val selectedTime = DateTime(it)
                                if (selectedTime.isBeforeNow){
                                    errorMessage = "You cannot set a date in the past"
                                } else {
                                    onMembershipDueDateUpdate.invoke(selectedTime)
                                }
                            }
                        },
                        shape = RoundedCornerShape(border_radius),
                        border = BorderStroke(1.dp, color = AppTheme.colors.highLight),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        TextRegular(text = "Update")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = padding_screen))
        ButtonWidget(title = "View Attendance", isEnabled = true) {
            onViewAttendance.invoke(memberEntity)
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        MemberEditDetailsScreen(
            memberEntity = getMockMemberEntity(),
        )
    }
}
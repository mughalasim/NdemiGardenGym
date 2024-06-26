package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.MemberProfileWidget
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
    val context = LocalContext.current
    val galleryLauncher =  rememberLauncherForActivityResult(GetContent()) { imageUri ->
        imageUri?.let {
            context.contentResolver.openInputStream(imageUri)?.use { it.buffered().readBytes()}?.let {
                viewModel.updateMemberImage(it)
            }
        }
    }
    LaunchedEffect(true) { viewModel.getMemberForId(memberId) }

    Column {
        ToolBarWidget(title = "Edit Member", canNavigateBack = true) {
            viewModel.navigateBack()
        }

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMemberForId(memberId) }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.value is UiState.Success) {
                    val memberEntity = (uiState.value as UiState.Success).memberEntity

                    MemberProfileWidget(
                        imageUrl = memberEntity.profileImageUrl,
                        onImageSelect = {
                            galleryLauncher.launch("image/*")
                        },
                        onImageDelete = {
                            viewModel.deleteMemberImage()
                        }
                    )

                    MemberEditDetailsScreen(
                        memberEntity = memberEntity,
                        onCoachSetUpdate = {
                          viewModel.onCoachSetUpdate(it)
                        },
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
}

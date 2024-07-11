package com.ndemi.garden.gym.ui.screens.profile

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
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberProfileWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = koinViewModel<ProfileScreenViewModel>()
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

    LaunchedEffect(true) { viewModel.getMember() }

    Column {
        ToolBarWidget(title = stringResource(R.string.txt_profile))

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMember() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(padding_screen),
            ) {
                if (uiState.value is UiState.Success) {
                    MemberProfileWidget(
                        imageUrl = (uiState.value as UiState.Success).memberEntity.profileImageUrl,
                        onImageDelete = {
                            viewModel.deleteMemberImage()
                        },
                        onImageSelect = {
                            galleryLauncher.launch("image/*")
                        }
                    )
                    ProfileDetailsScreen(
                        memberEntity = (uiState.value as UiState.Success).memberEntity,
                        isAdmin = viewModel.isAdmin(),
                        message = (uiState.value as UiState.Success).message,
                        sessionStartTime = sessionStartTime,
                        onSessionStarted = viewModel::setStartedSession,
                        onSessionCompleted = viewModel::setAttendance,
                    )
                    ButtonWidget(
                        title = stringResource(R.string.txt_logout),
                    ){
                        viewModel.onLogOutTapped()
                    }
                }
            }
        }
    }
}

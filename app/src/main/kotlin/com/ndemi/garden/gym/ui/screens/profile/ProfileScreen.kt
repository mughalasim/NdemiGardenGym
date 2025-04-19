package com.ndemi.garden.gym.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = koinViewModel<ProfileScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    LaunchedEffect(Unit) { viewModel.observeMember() }
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading).value
    val sessionStartTime = viewModel.sessionStartTime.collectAsState().value
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_profile),
            secondaryIcon = Icons.AutoMirrored.Filled.Logout,
            onSecondaryIconPressed = viewModel::onLogOutTapped,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(padding_screen),
        ) {
            when (uiState) {
                is UiState.Success -> {
                    if (uiState.errorMessage.isNotEmpty()) {
                        snackbarHostState.Show(
                            type = SnackbarType.ERROR,
                            message = uiState.errorMessage,
                        )
                    }
                    MemberImageWidget(
                        imageUrl = uiState.memberEntity.profileImageUrl,
                        onImageDelete = {
                            viewModel.deleteMemberImage()
                        },
                        onImageSelect = {
                            galleryLauncher.launch("image/*")
                        },
                    )
                    ProfileDetailsScreen(
                        memberEntity = uiState.memberEntity,
                        isAdmin = viewModel.isAdmin(),
                        message = uiState.errorMessage,
                        sessionStartTime = sessionStartTime,
                        onSessionStarted = viewModel::setStartedSession,
                        onSessionCompleted = viewModel::setAttendance,
                    )
                }
                is UiState.Loading -> {
                    LoadingScreenWidget()
                }
            }
        }
    }
}

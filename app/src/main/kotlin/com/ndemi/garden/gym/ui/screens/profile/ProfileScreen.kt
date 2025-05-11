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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AlertDialogWidget
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
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val sessionStartTime by viewModel.sessionStartTime.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }

    if (showDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_are_you_sure),
            message = stringResource(R.string.txt_are_you_sure_log_out),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_logout),
            positiveOnClick = {
                showDialog = !showDialog
                viewModel.onLogOutTapped()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = {
                showDialog = !showDialog
            },
        )
    }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_profile),
            secondaryIcon = Icons.AutoMirrored.Filled.Logout,
            onSecondaryIconPressed = { showDialog = !showDialog },
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = padding_screen),
        ) {
            when (val state = uiState) {
                is UiState.Success -> {
                    if (state.errorMessage.isNotEmpty()) {
                        snackbarHostState.Show(
                            type = SnackbarType.ERROR,
                            message = state.errorMessage,
                        )
                    }
                    MemberImageWidget(
                        imageUrl = state.memberEntity.profileImageUrl,
                        onImageDelete = {
                            viewModel.deleteMemberImage()
                        },
                        onImageSelect = {
                            galleryLauncher.launch("image/*")
                        },
                    )
                    ProfileDetailsScreen(
                        memberEntity = state.memberEntity,
                        isAdmin = viewModel.isAdmin(),
                        message = state.errorMessage,
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

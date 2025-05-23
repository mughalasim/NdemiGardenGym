package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileMemberScreen(
    viewModel: ProfileMemberScreenViewModel = koinViewModel<ProfileMemberScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val countdown by viewModel.countdown.collectAsStateWithLifecycle()
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }

    Column {
        when (val state = uiState) {
            is UiState.Success ->
                ProfileMemberDetailsScreen(
                    state = state,
                    countdown = countdown,
                    listeners =
                        ProfileMemberScreenListeners(
                            onImageDeleted = viewModel::onImageDeleted,
                            onImageSelected = {
                                galleryLauncher.launch("image/*")
                            },
                            onEditDetailsTapped = viewModel::onEditDetailsTapped,
                            onSessionTapped = viewModel::onSessionTapped,
                            onLogoutTapped = viewModel::onLogOutTapped,
                        ),
                )

            is UiState.Loading ->
                LoadingScreenWidget()
        }
    }
}

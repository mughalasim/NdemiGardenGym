package com.ndemi.garden.gym.ui.screens.profile.superadmin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSuperAdminScreen(
    viewModel: ProfileMemberScreenViewModel = koinViewModel<ProfileMemberScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }

    ProfileSuperAdminDetailsScreen(
        uiState = uiState,
        listener =
            ProfileSuperAdminListeners(
                deleteMemberImage = viewModel::onImageDeleted,
                onImageSelect = { galleryLauncher.launch("image/*") },
                onLogoutTapped = viewModel::onLogOutTapped,
            ),
    )
}

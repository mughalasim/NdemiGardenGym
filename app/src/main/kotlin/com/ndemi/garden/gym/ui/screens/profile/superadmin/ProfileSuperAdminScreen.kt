package com.ndemi.garden.gym.ui.screens.profile.superadmin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSuperAdminScreen(
    viewModel: ProfileMemberScreenViewModel = koinViewModel<ProfileMemberScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)

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
            negativeOnClick = { showDialog = !showDialog },
        )
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

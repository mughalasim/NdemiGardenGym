package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileMemberScreen(
    viewModel: ProfileMemberScreenViewModel = koinViewModel<ProfileMemberScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val countdown by viewModel.countdown.collectAsStateWithLifecycle()
    val weightState by viewModel.weightState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }
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

        when (val state = uiState) {
            is UiState.Success ->
                ProfileMemberDetailsScreen(
                    state = state,
                    weightState = weightState,
                    countdown = countdown,
                    listeners =
                        ProfileMemberScreenListeners(
                            onImageDeleted = viewModel::onImageDeleted,
                            onImageSelected = {
                                galleryLauncher.launch("image/*")
                            },
                            onEditDetailsTapped = viewModel::onEditDetailsTapped,
                            onSessionTapped = viewModel::onSessionTapped,
                            onDeleteWeightTapped = viewModel::onDeleteWeightTapped,
                            onAddWeightTapped = viewModel::onAddWeightTapped,
                            onWeightValueChanged = viewModel::onWeightValueChanged,
                        ),
                )

            is UiState.Loading ->
                LoadingScreenWidget()
        }
    }
}

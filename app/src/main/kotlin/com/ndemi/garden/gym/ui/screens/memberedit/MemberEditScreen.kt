package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MemberEditScreen(
    memberId: String,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val permissionState by viewModel.getPermissions().collectAsStateWithLifecycle()
    val memberEntity by viewModel.memberEntity.collectAsStateWithLifecycle()
    var showDeleteUserDialog by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)

    LaunchedEffect(Unit) { viewModel.getMemberForId(memberId) }

    if (showDeleteUserDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_are_you_sure),
            message = stringResource(R.string.txt_are_you_sure_delete_member),
            onDismissed = { showDeleteUserDialog = !showDeleteUserDialog },
            positiveButton = stringResource(R.string.txt_delete),
            positiveOnClick = {
                showDeleteUserDialog = !showDeleteUserDialog
                viewModel.deleteMember()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = {
                showDeleteUserDialog = !showDeleteUserDialog
            },
        )
    }

    MemberEditDetailsScreen(
        uiState = uiState,
        permissionState = permissionState,
        memberEntity = memberEntity,
        toolbarTitle =
            if (memberId.isEmpty()) {
                stringResource(R.string.txt_edit_your_details)
            } else {
                stringResource(R.string.txt_edit_member)
            },
        listeners =
            MemberEditScreenListeners(
                onImageSelect = { galleryLauncher.launch("image/*") },
                onImageDelete = viewModel::deleteMemberImage,
                onSetString = viewModel::setString,
                onUpdateTapped = viewModel::onUpdateTapped,
                onDeleteMemberTapped = { showDeleteUserDialog = true },
                onBackTapped = viewModel::navigateBack,
            ),
    )
}

data class MemberEditScreenListeners(
    val onImageSelect: () -> Unit = {},
    val onImageDelete: () -> Unit = {},
    val onSetString: (String, MemberEditScreenInputType) -> Unit = { _, _ -> },
    val onUpdateTapped: () -> Unit = {},
    val onDeleteMemberTapped: () -> Unit = {},
    val onBackTapped: () -> Unit = {},
)

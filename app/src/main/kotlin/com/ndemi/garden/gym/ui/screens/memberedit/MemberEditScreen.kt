package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.screens.ImageSelector
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import cv.domain.enums.MemberType
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MemberEditScreen(
    memberId: String,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    imageSelector: ImageSelector = ImageSelector(),
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>(parameters = { parametersOf(memberId) }),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val permissionState by viewModel.getPermissions().collectAsStateWithLifecycle()
    val model by viewModel.memberModel.collectAsStateWithLifecycle()
    var showDeleteUserDialog by remember { mutableStateOf(false) }
    var showMemberTypeSelectionDialog by remember { mutableStateOf(false) }
    imageSelector.SetUpResult(LocalContext.current) {
        viewModel.updateMemberImage(it)
    }
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)

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
    if (showMemberTypeSelectionDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_update_member_type),
            message = stringResource(R.string.txt_update_member_type_desc),
            onDismissed = { showMemberTypeSelectionDialog = !showMemberTypeSelectionDialog },
            listItems = MemberType.entries.map { it.name },
            positiveButton = stringResource(R.string.txt_cancel),
            positiveOnClick = {
                showMemberTypeSelectionDialog = !showMemberTypeSelectionDialog
            },
            onListItemClicked = {
                showMemberTypeSelectionDialog = !showMemberTypeSelectionDialog
                viewModel.setNewMemberType(it)
            },
        )
    }

    MemberEditDetailsScreen(
        uiState = uiState,
        permissionState = permissionState,
        model = model,
        toolbarTitle =
            if (memberId.isEmpty()) {
                stringResource(R.string.txt_edit_your_details)
            } else {
                stringResource(R.string.txt_edit_member)
            },
        listeners =
            MemberEditScreenListeners(
                onImageSelect = imageSelector::openImages,
                onImageDelete = viewModel::deleteMemberImage,
                onSetString = viewModel::setString,
                onUpdateTapped = viewModel::onUpdateTapped,
                onDeleteMemberTapped = { showDeleteUserDialog = true },
                onMemberTypeTapped = { showMemberTypeSelectionDialog = true },
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
    val onMemberTypeTapped: () -> Unit = {},
)

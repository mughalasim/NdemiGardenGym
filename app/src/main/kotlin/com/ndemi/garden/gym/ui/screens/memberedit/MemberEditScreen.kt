package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberProfileWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreen(
    memberId: String,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>(),
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val memberEntity = viewModel.memberEntity.collectAsState()
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(GetContent()) { imageUri ->
            imageUri?.let {
                context.contentResolver.openInputStream(imageUri)
                    ?.use { inputStream -> inputStream.buffered().readBytes() }
                    ?.let { byteArray -> viewModel.updateMemberImage(byteArray) }
            }
        }
    var showDeleteUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) { viewModel.getMemberForId(memberId) }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_edit_member),
            canNavigateBack = true,
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.DeleteForever else null,
            onSecondaryIconPressed = { showDeleteUserDialog = true },
            onBackPressed = viewModel::navigateBack,
        )

        PullToRefreshBox(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding_screen),
            isRefreshing = (uiState.value is UiState.Loading),
            contentAlignment = Alignment.TopCenter,
            onRefresh = { viewModel.getMemberForId(memberId) },
        ) {
            Column(
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .requiredWidth(page_width),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MemberProfileWidget(
                    isEnabled = viewModel.hasAdminRights(),
                    imageUrl = memberEntity.value.profileImageUrl,
                    onImageSelect = {
                        galleryLauncher.launch("image/*")
                    },
                    onImageDelete = {
                        viewModel.deleteMemberImage()
                    },
                )

                MemberEditDetailsScreen(
                    hasAdminRights = viewModel.hasAdminRights(),
                    uiState = uiState.value,
                    memberEntity = memberEntity.value,
                    onSetString = viewModel::setString,
                    snackbarHostState = snackbarHostState,
                    onUpdateTapped = viewModel::onUpdateTapped,
                )
            }
        }

        if (showDeleteUserDialog) {
            AlertDialog(
                containerColor = AppTheme.colors.backgroundButtonDisabled,
                title = {
                    TextWidget(
                        text = stringResource(R.string.txt_are_you_sure),
                        style = AppTheme.textStyles.regularBold,
                    )
                },
                text = {
                    TextWidget(
                        text = stringResource(R.string.txt_are_you_sure_delete_member),
                    )
                },
                onDismissRequest = { showDeleteUserDialog = !showDeleteUserDialog },
                confirmButton = {
                    ButtonWidget(title = stringResource(R.string.txt_delete)) {
                        showDeleteUserDialog = !showDeleteUserDialog
                        viewModel.deleteMember()
                    }
                },
                dismissButton = {
                    ButtonWidget(title = stringResource(R.string.txt_cancel)) {
                        showDeleteUserDialog = !showDeleteUserDialog
                    }
                },
            )
        }
    }
}

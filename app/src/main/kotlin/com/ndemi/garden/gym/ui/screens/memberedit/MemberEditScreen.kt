package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberProfileWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreen(
    memberId: String,
    viewModel: MemberEditScreenViewModel = koinViewModel<MemberEditScreenViewModel>(),
) {
    viewModel.setMemberId(memberId)
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val memberEntity = viewModel.memberEntity.observeAsState()
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(GetContent()) { imageUri ->
        imageUri?.let {
            context.contentResolver.openInputStream(imageUri)?.use { it.buffered().readBytes() }
                ?.let {
                    viewModel.updateMemberImage(it)
                }
        }
    }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) { viewModel.getMemberForId() }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_edit_member),
            canNavigateBack = true,
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.DeleteForever else null,
            onSecondaryIconPressed = { showDialog = true },
            onBackPressed = viewModel::navigateBack
        )

        if (uiState.value is UiState.Error) {
            WarningWidget((uiState.value as UiState.Error).message)
        }

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_screen),
            isRefreshing = (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMemberForId() }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                memberEntity.value?.let {
                    MemberProfileWidget(
                        isEnabled = viewModel.hasAdminRights(),
                        imageUrl = it.profileImageUrl,
                        onImageSelect = {
                            galleryLauncher.launch("image/*")
                        },
                        onImageDelete = {
                            viewModel.deleteMemberImage()
                        }
                    )

                    MemberEditDetailsScreen(
                        hasAdminRights = viewModel.hasAdminRights(),
                        uiState = uiState,
                        memberEntity = it,
                        onSetString = viewModel::setString,
                        onUpdateTapped = viewModel::onUpdateTapped,
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                containerColor = AppTheme.colors.backgroundButtonDisabled,
                title = { TextSmall(text = stringResource(R.string.txt_are_you_sure)) },
                text = {
                    TextRegular(
                        text = stringResource(R.string.txt_are_you_sure_delete_member)
                    )
                },
                onDismissRequest = { showDialog = !showDialog },
                confirmButton = {
                    ButtonWidget(title = stringResource(R.string.txt_delete)) {
                        showDialog = !showDialog
                        viewModel.deleteMember()
                    }
                },
                dismissButton = {
                    ButtonWidget(title = stringResource(R.string.txt_cancel)) {
                        showDialog = !showDialog
                    }
                })
        }
    }
}

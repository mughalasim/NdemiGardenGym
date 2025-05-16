package com.ndemi.garden.gym.ui.screens.profile.superadmin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget

@Composable
internal fun ProfileSuperAdminDetailsScreen(
    uiState: ProfileScreenViewModel.UiState = ProfileScreenViewModel.UiState.Success(memberEntity = getMockActiveMemberEntity()),
    listener: ProfileSuperAdminListeners = ProfileSuperAdminListeners(),
) {
    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_profile),
            secondaryIcon = Icons.AutoMirrored.Filled.Logout,
            onSecondaryIconPressed = listener.onLogoutTapped,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = padding_screen),
        ) {
            when (uiState) {
                is ProfileScreenViewModel.UiState.Success -> {
                    MemberImageWidget(
                        imageUrl = uiState.memberEntity.profileImageUrl,
                        onImageDelete = listener.deleteMemberImage,
                        onImageSelect = listener.onImageSelect,
                    )
                }

                is ProfileScreenViewModel.UiState.Loading -> LoadingScreenWidget()
            }
        }
    }
}

internal data class ProfileSuperAdminListeners(
    val onImageSelect: () -> Unit = {},
    val deleteMemberImage: () -> Unit = {},
    val onLogoutTapped: () -> Unit = {},
)

@AppPreview
@Composable
private fun ProfileSuperAdminDetailsScreenPreview() = AppThemeComposable {
    ProfileSuperAdminDetailsScreen()
}

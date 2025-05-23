package com.ndemi.garden.gym.ui.screens.profile.superadmin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.screens.profile.ProfileTopSection
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget

@Composable
internal fun ProfileSuperAdminDetailsScreen(
    uiState: ProfileMemberScreenViewModel.UiState =
        ProfileMemberScreenViewModel.UiState.Success(memberEntity = getMockActiveMemberEntity()),
    listener: ProfileSuperAdminListeners = ProfileSuperAdminListeners(),
) {
    Column {
        ProfileTopSection(onLogoutTapped = listener.onLogoutTapped)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = padding_screen),
        ) {
            when (uiState) {
                is ProfileMemberScreenViewModel.UiState.Success -> {
                    MemberImageWidget(
                        imageUrl = uiState.memberEntity.profileImageUrl,
                        onImageDelete = listener.deleteMemberImage,
                        onImageSelect = listener.onImageSelect,
                    )
                }

                is ProfileMemberScreenViewModel.UiState.Loading -> LoadingScreenWidget()
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
private fun ProfileSuperAdminDetailsScreenPreview() =
    AppThemeComposable {
        ProfileSuperAdminDetailsScreen()
    }

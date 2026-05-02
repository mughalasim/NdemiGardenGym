package com.ndemi.garden.gym.ui.screens.members

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberPresentationModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import cv.domain.entities.PermissionsEntity
import cv.domain.presentationModels.MemberPresentationModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MembersSharedScreen(
    @StringRes pageTitleRes: Int = R.string.txt_active_members,
    @StringRes defaultMessageRes: Int = R.string.txt_no_members,
    screenType: MemberScreenType = MemberScreenType.ALL_MEMBERS,
    permissionState: PermissionsEntity = PermissionsEntity(),
    searchTerm: String = "",
    uiState: UiState = UiState.Loading,
    members: List<MemberPresentationModel> = listOf(),
    listeners: MembersSharedScreenListeners = MembersSharedScreenListeners(),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            ToolBarWidget(
                title = stringResource(pageTitleRes),
                secondaryIcon = if (permissionState.canAddMember) Icons.Default.PersonAdd else null,
                onSecondaryIconPressed = listeners.onRegisterMemberTapped,
            )

            SearchMemberComponent(
                textInput = searchTerm,
                isVisible = members.isNotEmpty() || searchTerm.isNotEmpty(),
                memberCount = members.size,
                onTextChanged = listeners.onSearchTextChanged,
            )

            if (members.isEmpty() && uiState !is UiState.Loading) {
                TextWidget(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(padding_screen),
                    textAlign = TextAlign.Center,
                    text = stringResource(defaultMessageRes),
                )
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(horizontal = padding_screen),
                verticalItemSpacing = padding_screen_tiny,
                horizontalArrangement = Arrangement.spacedBy(padding_screen_tiny),
                columns = StaggeredGridCells.Fixed(COLUMN_COUNT),
            ) {
                items(members) {
                    MemberStatusWidget(
                        model = it,
                        canViewMemberDetails = permissionState.canViewMemberDetails,
                        canViewMemberStats = permissionState.canViewMemberStats && screenType != MemberScreenType.NON_MEMBERS,
                        listener = listeners.memberStatusWidgetListener,
                    )
                }
            }
        }

        if (uiState is UiState.Loading) {
            LoadingScreenWidget(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(padding_screen)
                        .toAppCardStyle(),
                customMessage = "Updating members...",
            )
        }
    }
}

data class MembersSharedScreenListeners(
    val onRegisterMemberTapped: () -> Unit = {},
    val onSearchTextChanged: (String) -> Unit = {},
    val memberStatusWidgetListener: MemberStatusWidgetListener = MemberStatusWidgetListener(),
)

@AppPreview
@Composable
private fun MembersSharedScreenPreview() =
    AppThemeComposable {
        MembersSharedScreen(
            uiState = UiState.Success,
            members =
                listOf(
                    getMockRegisteredMemberPresentationModel(),
                    getMockActiveMemberPresentationModel(),
                    getMockExpiredMemberPresentationModel(),
                    getMockRegisteredMemberPresentationModel(),
                    getMockExpiredMemberPresentationModel(),
                    getMockRegisteredMemberPresentationModel(),
                    getMockRegisteredMemberPresentationModel(),
                ),
        )
    }

private const val COLUMN_COUNT = 3

package com.ndemi.garden.gym.ui.screens.members

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import cv.domain.entities.MemberEntity

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MembersSharedScreen(
    @StringRes pageTitleRes: Int = R.string.txt_active_members,
    @StringRes defaultMessageRes: Int = R.string.txt_no_members,
    hasAdminRights: Boolean = false,
    searchTerm: String = "",
    uiState: UiState = UiState.Loading,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    members: List<MemberEntity> = listOf(),
    listeners: MembersSharedScreenListeners = MembersSharedScreenListeners(),
) {
    Column {
        ToolBarWidget(
            title = stringResource(pageTitleRes),
            secondaryIcon = if (hasAdminRights) Icons.Default.PersonAdd else null,
            onSecondaryIconPressed = listeners.onRegisterMemberTapped,
        )

        if (uiState is UiState.Error) {
            snackbarHostState.Show(
                type = SnackbarType.ERROR,
                message = uiState.message,
            )
        }

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

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = uiState is UiState.Loading,
            onRefresh = { listeners.getMembers() },
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(horizontal = padding_screen),
                verticalItemSpacing = padding_screen_small,
                horizontalArrangement = Arrangement.spacedBy(padding_screen_small),
                columns = StaggeredGridCells.Fixed(2),
            ) {
                items(members) {
                    MemberStatusWidget(
                        memberEntity = it,
                        hasAdminRights = hasAdminRights,
                        listener = listeners.memberStatusWidgetListener,
                    )
                }
            }
        }
    }
}

data class MembersSharedScreenListeners(
    val onRegisterMemberTapped: () -> Unit = {},
    val onSearchTextChanged: (String) -> Unit = {},
    val getMembers: () -> Unit = {},
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
                    getMockRegisteredMemberEntity(),
                    getMockActiveMemberEntity(),
                    getMockExpiredMemberEntity(),
                    getMockRegisteredMemberEntity(),
                    getMockExpiredMemberEntity(),
                    getMockRegisteredMemberEntity(),
                    getMockRegisteredMemberEntity(),
                ),
            hasAdminRights = true,
        )
    }

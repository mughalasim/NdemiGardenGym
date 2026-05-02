package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener

@Composable
fun MembersScreen(viewModel: MembersScreenViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val members = viewModel.members
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val permissionState by viewModel.getPermissions().collectAsStateWithLifecycle()

    MembersSharedScreen(
        pageTitleRes = R.string.txt_all_members,
        defaultMessageRes = R.string.txt_no_active_registered_members,
        screenType = MemberScreenType.ALL_MEMBERS,
        permissionState = permissionState,
        searchTerm = searchTerm,
        uiState = uiState,
        members = members,
        listeners =
            MembersSharedScreenListeners(
                onRegisterMemberTapped = viewModel::onRegisterMember,
                onSearchTextChanged = viewModel::onSearchTextChanged,
                memberStatusWidgetListener =
                    MemberStatusWidgetListener(
                        onMemberTapped = viewModel::onMemberTapped,
                        onPaymentsTapped = viewModel::onPaymentsTapped,
                        onAttendanceTapped = viewModel::onAttendanceTapped,
                        onSessionTapped = viewModel::onSessionTapped,
                    ),
            ),
    )
}

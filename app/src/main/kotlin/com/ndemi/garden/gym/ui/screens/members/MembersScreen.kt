package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import org.koin.androidx.compose.koinViewModel

@Composable
fun MembersScreen(
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val members by viewModel.members.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMembers(
            memberScreenType = MemberScreenType.ALL_MEMBERS,
        )
    }

    MembersSharedScreen(
        pageTitleRes = R.string.txt_active_members,
        defaultMessageRes = R.string.txt_no_active_registered_members,
        hasAdminRights = viewModel.hasAdminRights(),
        searchTerm = searchTerm,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        members = members,
        listeners =
            MembersSharedScreenListeners(
                onRegisterMemberTapped = viewModel::onRegisterMember,
                onSearchTextChanged = viewModel::onSearchTextChanged,
                getMembers = {
                    viewModel.getMembers(MemberScreenType.ALL_MEMBERS)
                },
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

package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import org.koin.androidx.compose.koinViewModel

@Composable
fun MembersExpiredScreen(
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val permissionState by viewModel.getPermissions().collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMembers(
            memberScreenType = MemberScreenType.EXPIRED_MEMBERS,
        )
    }

    MembersSharedScreen(
        pageTitleRes = R.string.txt_expired_memberships,
        defaultMessageRes = R.string.txt_no_members,
        permissionState = permissionState,
        searchTerm = searchTerm,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        members = members,
        listeners =
            MembersSharedScreenListeners(
                onRegisterMemberTapped = viewModel::onRegisterMember,
                onSearchTextChanged = viewModel::onSearchTextChanged,
                getMembers = {
                    viewModel.getMembers(MemberScreenType.EXPIRED_MEMBERS)
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

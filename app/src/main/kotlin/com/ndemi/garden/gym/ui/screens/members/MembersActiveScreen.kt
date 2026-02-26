package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MembersActiveScreen(
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>(parameters = { parametersOf(MemberScreenType.LIVE_MEMBERS) }),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val members = viewModel.members
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()

    MembersSharedScreen(
        pageTitleRes = R.string.txt_who_is_in,
        defaultMessageRes = R.string.txt_no_active_members,
        screenType = MemberScreenType.LIVE_MEMBERS,
        searchTerm = searchTerm,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
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

package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ObserveAppSnackbar
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileAdminScreen(
    viewModel: ProfileMemberScreenViewModel = koinViewModel<ProfileMemberScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    viewModel.snackbarState.ObserveAppSnackbar(snackbarHostState)

    // TODO -  Profile should include the following data
    // total Registered users - 82
    // Not renewed Membership - 20
    // total revenue this year - Kes 9000
    // total revenue this month - Kes 250
    // 10 top paying members -  List of members and their amounts spent
    // 10 top active members - List or members and attendance count

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreenWidget()

        is UiState.Success ->
            ProfileAdminScreenDetails(
                onLogoutTapped = viewModel::onLogOutTapped,
            )
    }
}

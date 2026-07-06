package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileAdminScreen(viewModel: ProfileAdminScreenViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    ProfileAdminScreenDetails(
        uiState = uiState,
        listeners =
            ProfileAdminScreenDetailsListeners(
                onSettingsTapped = viewModel::onSettingsTapped,
                onYearPlusTapped = viewModel::onYearPlusTapped,
                onYearMinusTapped = viewModel::onYearMinusTapped,
                onMonthPlusTapped = viewModel::onMonthPlusTapped,
                onMonthMinusTapped = viewModel::onMonthMinusTapped,
                onMemberTapped = viewModel::onMemberTapped,
            ),
    )
}

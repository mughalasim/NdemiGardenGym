package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.screens.ImageSelector
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget

@Composable
fun ProfileMemberScreen(
    viewModel: ProfileMemberScreenViewModel,
    imageSelector: ImageSelector = ImageSelector(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val countdown by viewModel.countdown.collectAsStateWithLifecycle()
    val sessionStartTime by viewModel.sessionStartTime.collectAsStateWithLifecycle()
    imageSelector.SetUpResult(LocalContext.current) {
        viewModel.updateMemberImage(it)
    }

    Column {
        when (val state = uiState) {
            is UiState.Success -> {
                ProfileMemberDetailsScreen(
                    state = state,
                    countdown = countdown,
                    sessionStartTime = sessionStartTime,
                    listeners =
                        ProfileMemberScreenListeners(
                            onSettingsTapped = viewModel::onSettingsTapped,
                            onEditDetailsTapped = viewModel::onEditDetailsTapped,
                            onSessionTapped = viewModel::onSessionTapped,
                            onImageDeleted = viewModel::onImageDeleted,
                            onImageSelected = imageSelector::openImages,
                        ),
                )
            }

            is UiState.Loading -> {
                LoadingScreenWidget()
            }
        }
    }
}

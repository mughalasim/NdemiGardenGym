package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveAttendanceScreen(
    viewModel: LiveAttendanceScreenViewModel = koinViewModel<LiveAttendanceScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()

    LaunchedEffect(Unit) { viewModel.getLiveMembers() }

    Column {
        ToolBarWidget(title = stringResource(R.string.txt_who_is_in))

        PullToRefreshBox(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding_screen),
            isRefreshing = (uiState is UiState.Loading),
            onRefresh = { viewModel.getLiveMembers() },
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (val state = uiState) {
                    is UiState.Error -> {
                        snackbarHostState.Show(
                            type = SnackbarType.ERROR,
                            message = state.message,
                        )
                    }

                    is UiState.Success -> {
                        if (state.members.isEmpty()) {
                            TextWidget(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(padding_screen),
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.txt_no_one_is_in),
                            )
                        } else {
                            LiveAttendanceListScreen(members = state.members)
                        }
                    }

                    UiState.Loading -> Unit
                }
            }
        }
    }
}

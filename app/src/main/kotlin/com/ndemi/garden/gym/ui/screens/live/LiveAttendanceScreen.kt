package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveAttendanceScreen(
    viewModel: LiveAttendanceScreenViewModel = koinViewModel<LiveAttendanceScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) { viewModel.getLiveMembers() }

    Column {
        ToolBarWidget(title = stringResource(R.string.txt_who_is_in))

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getLiveMembers() }
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (uiState.value is UiState.Success) {
                    if ((uiState.value as UiState.Success).members.isEmpty()) {
                        TextRegular(
                            text = stringResource(R.string.txt_no_one_is_in)
                        )
                    } else {
                        LiveAttendanceListScreen(members = (uiState.value as UiState.Success).members)
                    }
                }
            }
        }
    }
}

package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen (
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) { viewModel.getMembers() }

    Column {
        ToolBarWidget(title = "Members")

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMembers() }
        ){
            if (uiState.value is UiState.Success) {
                if ((uiState.value as UiState.Success).members.isEmpty()){
                    WarningWidget(title = "No registered members")
                } else {
                    MembersListScreen(
                        members = (uiState.value as UiState.Success).members,
                        onMemberTapped = viewModel::onMemberTapped
                    )
                }
            }
        }
    }
}

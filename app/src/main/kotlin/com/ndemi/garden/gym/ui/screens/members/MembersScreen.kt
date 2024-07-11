package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.TextRegular
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
        ToolBarWidget(
            title = stringResource(R.string.txt_members),
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.PersonAdd else null,
            onSecondaryIconPressed = viewModel::onRegisterMember
        )

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_screen),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMembers() }
        ){
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (uiState.value is UiState.Success) {
                    if ((uiState.value as UiState.Success).members.isEmpty()){
                        TextRegular(text = stringResource(R.string.txt_no_members))
                    } else {
                        MembersListScreen(
                            hasAdminRights = viewModel.hasAdminRights(),
                            members = (uiState.value as UiState.Success).members,
                            onMemberTapped = viewModel::onMemberTapped,
                            onPaymentsTapped = viewModel::onPaymentsTapped,
                            onAttendanceTapped = viewModel::onAttendanceTapped,
                            onSessionTapped = viewModel::onSessionTapped
                        )
                    }
                }
            }
        }
    }
}

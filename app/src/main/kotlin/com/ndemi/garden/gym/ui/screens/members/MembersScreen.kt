package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.SearchTextWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>()
) {
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)
    val members = viewModel.members.observeAsState(initial = listOf())

    LaunchedEffect(true) { viewModel.getMembers() }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_all_members),
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.PersonAdd else null,
            onSecondaryIconPressed = viewModel::onRegisterMember
        )

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding_screen),
            isRefreshing = (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getMembers() }
        ) {
            LazyColumn {
                item {
                    SearchTextWidget(
                        textInput = viewModel.searchTerm,
                        hint = stringResource(R.string.txt_search_members),
                        onValueChanged = viewModel::onSearchTextChanged
                    )
                }
                item {
                    if (members.value.isEmpty() && uiState.value !is UiState.Loading) {
                        TextRegular(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding_screen),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.txt_no_members)
                        )
                    }
                }
                items(members.value) {
                    MemberStatusWidget(
                        memberEntity = it,
                        showDetails = true,
                        hasAdminRights = viewModel.hasAdminRights(),
                        onMemberTapped = viewModel::onMemberTapped,
                        onPaymentsTapped = viewModel::onPaymentsTapped,
                        onAttendanceTapped = viewModel::onAttendanceTapped,
                        onSessionTapped = viewModel::onSessionTapped,
                    )
                }
            }
        }
    }
}

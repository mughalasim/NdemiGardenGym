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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidgetListener
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersExpiredScreen(
    viewModel: MembersScreenViewModel = koinViewModel<MembersScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    val members by viewModel.members.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMembers(
            memberScreenType = MemberScreenType.EXPIRED_MEMBERS,
        )
    }

    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_expired_memberships),
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.PersonAdd else null,
            onSecondaryIconPressed = viewModel::onRegisterMember,
        )

        if (uiState is UiState.Error) {
            snackbarHostState.Show(
                type = SnackbarType.ERROR,
                message = (uiState as UiState.Error).message,
            )
        }

        PullToRefreshBox(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = padding_screen),
            isRefreshing = (uiState is UiState.Loading),
            onRefresh = { viewModel.getMembers(MemberScreenType.EXPIRED_MEMBERS) },
        ) {
            LazyColumn {
                item {
                    SearchMemberComponent(
                        textInput = searchTerm,
                        isVisible = members.isNotEmpty() || searchTerm.isNotEmpty(),
                        memberCount = members.size,
                        onTextChanged = viewModel::onSearchTextChanged,
                    )
                }
                item {
                    if (members.isEmpty() && uiState !is UiState.Loading) {
                        TextWidget(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(padding_screen),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.txt_no_members),
                        )
                    }
                }
                items(members) {
                    MemberStatusWidget(
                        memberEntity = it,
                        hasAdminRights = viewModel.hasAdminRights(),
                        listener =
                            MemberStatusWidgetListener(
                                onMemberTapped = viewModel::onMemberTapped,
                                onPaymentsTapped = viewModel::onPaymentsTapped,
                                onAttendanceTapped = viewModel::onAttendanceTapped,
                                onSessionTapped = viewModel::onSessionTapped,
                            ),
                    )
                }
            }
        }
    }
}

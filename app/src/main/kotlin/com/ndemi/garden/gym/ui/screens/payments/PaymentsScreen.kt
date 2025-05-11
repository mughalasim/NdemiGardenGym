package com.ndemi.garden.gym.ui.screens.payments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.AlertDialogWidget
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.YearSelectionWidget
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    memberId: String,
    memberName: String,
    viewModel: PaymentsScreenViewModel = koinViewModel<PaymentsScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    var showDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val canAddPayment by viewModel.canAddPayment.collectAsStateWithLifecycle()

    viewModel.setMemberId(memberId)

    LaunchedEffect(Unit) { viewModel.getPaymentsForMember() }

    Column {
        ToolBarWidget(
            title =
                if (memberName.isEmpty()) {
                    stringResource(R.string.txt_payments)
                } else {
                    stringResource(R.string.txt_payments_by, memberName)
                },
            secondaryIcon = if (viewModel.hasAdminRights()) Icons.Default.AddCircle else null,
            onSecondaryIconPressed = {
                if (canAddPayment) {
                    viewModel.navigateToPaymentAddScreen()
                } else {
                    showDialog = true
                }
            },
            canNavigateBack = memberId.isNotEmpty(),
            onBackPressed = viewModel::navigateBack,
        )

        YearSelectionWidget(
            selectedYear = selectedDate.year.toString(),
            isLoading = uiState is UiState.Loading,
            onYearPlusTapped = viewModel::increaseYear,
            onYearMinusTapped = viewModel::decreaseYear,
        )

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = uiState is UiState.Loading,
            onRefresh = { viewModel.getPaymentsForMember() },
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                when (val state = uiState) {
                    is UiState.Success -> {
                        if (state.payments.isEmpty()) {
                            TextWidget(
                                modifier = Modifier.fillMaxWidth().padding(padding_screen),
                                text = stringResource(R.string.txt_no_payments),
                                textAlign = TextAlign.Center,
                            )
                        } else {
                            PaymentsListScreen(
                                payments = state.payments,
                                totalAmount = state.totalAmount,
                                canDeletePayment = viewModel.hasAdminRights(),
                                onDeletePayment = viewModel::deletePayment,
                            )
                        }
                    }

                    is UiState.Error -> {
                        snackbarHostState.Show(
                            type = SnackbarType.ERROR,
                            message = state.message,
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    if (showDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_alert),
            message = stringResource(R.string.txt_cannot_add_payment),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_ok),
            positiveOnClick = {
                showDialog = !showDialog
            },
        )
    }
}

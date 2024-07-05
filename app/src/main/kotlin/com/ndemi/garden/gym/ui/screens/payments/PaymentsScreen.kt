package com.ndemi.garden.gym.ui.screens.payments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    memberId: String,
    memberName: String,
    viewModel: PaymentsScreenViewModel = koinViewModel<PaymentsScreenViewModel>()
) {
    var selectedDate by remember { mutableStateOf(DateTime.now()) }
    val canAddPayment = viewModel.canAddPayment.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) { viewModel.getPaymentsForMember(memberId, selectedDate) }

    Column {
        ToolBarWidget(
            title = if (memberName.isEmpty()) {
                stringResource(R.string.txt_payments)
            } else {
                stringResource(R.string.txt_payments_by, memberName)
            },
            secondaryIcon = if (memberId.isEmpty()) null else Icons.Default.AddCircle,
            onSecondaryIconPressed = {
                if (canAddPayment.value == true){
                    viewModel.navigateToPaymentAddScreen()
                } else {
                  showDialog = true
                }
            },
            canNavigateBack = memberId.isNotEmpty(),
            onBackPressed = viewModel::navigateBack
        )

        if (uiState.value is UiState.Error) WarningWidget((uiState.value as UiState.Error).message)

        DateSelectionWidget(selectedDate, true){
            selectedDate = it
            viewModel.getPaymentsForMember(memberId, selectedDate)
        }

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing= (uiState.value is UiState.Loading),
            onRefresh = { viewModel.getPaymentsForMember(memberId, selectedDate) }
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                if (uiState.value is UiState.Success) {
                    val result = (uiState.value as UiState.Success)
                    if (result.payments.isEmpty()) {
                        TextRegular(
                            modifier = Modifier.padding(padding_screen),
                            text = stringResource(R.string.txt_no_payments)
                        )
                    } else {
                        PaymentsListScreen(
                            payments = result.payments,
                            totalAmount = result.totalAmount,
                            canDeletePayment = memberId.isNotEmpty()
                        ){
                            viewModel.deletePayment(it)
                        }
                    }
                }
            }
        }
    }

    if (showDialog){
        AlertDialog(
            containerColor = AppTheme.colors.backgroundButtonDisabled,
            text = {
                TextRegular(
                    text = stringResource(R.string.txt_cannot_add_payment)
                )
            },
            onDismissRequest = { showDialog = !showDialog },
            confirmButton = {
                ButtonWidget(title = stringResource(id = R.string.txt_ok)) {
                    showDialog = !showDialog
                }
            },
        )
    }
}

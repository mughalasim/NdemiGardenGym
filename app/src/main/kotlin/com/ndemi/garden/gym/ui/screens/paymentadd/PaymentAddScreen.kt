package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentAddScreen(
    memberId: String,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    viewModel: PaymentAddScreenViewModel = koinViewModel<PaymentAddScreenViewModel>(),
) {
    viewModel.setMemberId(memberId)
    val uiState = viewModel.uiStateFlow.collectAsState(initial = UiState.Waiting)
    val inputData = viewModel.inputData.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBarWidget(
            title = stringResource(R.string.txt_add_payment),
            canNavigateBack = true,
            onBackPressed = viewModel::navigateBack,
        )

        PaymentAddDetailsScreen(
            inputData = inputData.value,
            uiState = uiState.value,
            onSetData = viewModel::setData,
            snackbarHostState = snackbarHostState,
            onPaymentAddTapped = viewModel::onPaymentAddTapped,
        )
    }
}

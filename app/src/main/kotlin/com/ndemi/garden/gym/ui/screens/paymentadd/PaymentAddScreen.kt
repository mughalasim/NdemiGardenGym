package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
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
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

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
            inputData = inputData,
            uiState = uiState,
            onSetData = viewModel::setData,
            snackbarHostState = snackbarHostState,
            onPaymentAddTapped = viewModel::onPaymentAddTapped,
        )
    }
}

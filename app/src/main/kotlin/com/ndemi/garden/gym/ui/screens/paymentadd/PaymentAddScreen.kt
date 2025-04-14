package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentAddScreen(
    memberId: String,
    viewModel: PaymentAddScreenViewModel = koinViewModel<PaymentAddScreenViewModel>()
) {
    viewModel.setMemberId(memberId)

    val uiState = viewModel.uiStateFlow.collectAsState(
        initial = UiState.Waiting
    )

    val inputData = viewModel.inputData.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBarWidget(title = stringResource(R.string.txt_add_payment),
            canNavigateBack = true,
            onBackPressed = viewModel::navigateBack
        )

        if (uiState.value is UiState.Error){
            val message = (uiState.value as UiState.Error).message
            WarningWidget(message)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = padding_screen)
        ) {
            PaymentAddDetailsScreen(
                startDate = inputData.value?.startDate,
                uiState = uiState.value,
                onSetData = viewModel::setData,
                onPaymentAddTapped = viewModel::onPaymentAddTapped
            )
        }
    }
}

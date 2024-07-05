package com.ndemi.garden.gym.ui.screens.payments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActivePaymentEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredPaymentEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.payments.PaymentWidget
import cv.domain.entities.PaymentEntity

@Composable
fun PaymentsListScreen(
    payments: List<PaymentEntity>,
    totalAmount: Double,
    canDeletePayment: Boolean,
    onDeletePayment: (PaymentEntity) -> Unit = {},
) {
    Column {
        Row {
            TextRegular(
                modifier = Modifier
                    .padding(horizontal = padding_screen)
                    .padding(top = padding_screen_small)
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.txt_total_amount,
                    totalAmount.toAmountString()
                ),
                textAlign = TextAlign.End
            )
        }
        repeat(payments.size) {
            PaymentWidget(
                paymentEntity = payments[it],
                canDeletePayment = canDeletePayment,
                onDeletePayment = onDeletePayment
            )
        }
    }
}

@AppPreview
@Composable
fun PaymentPlanListScreenPreview() {
    AppThemeComposable {
        PaymentsListScreen(
            payments = listOf(
                getMockActivePaymentEntity(),
                getMockActivePaymentEntity(),
                getMockExpiredPaymentEntity()
            ),
            totalAmount = 1200.0,
            canDeletePayment = false
        )
    }
}

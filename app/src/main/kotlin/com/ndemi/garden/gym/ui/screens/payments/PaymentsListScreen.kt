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
import com.ndemi.garden.gym.ui.mock.getMockActivePaymentPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockExpiredPaymentPresentationModel
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.payments.PaymentWidget
import cv.domain.presentationModels.PaymentPresentationModel

@Composable
fun PaymentsListScreen(
    models: List<PaymentPresentationModel>,
    totalAmount: String,
    canDeletePayment: Boolean,
    onDeletePayment: (PaymentPresentationModel) -> Unit = {},
) {
    Column {
        Row {
            TextWidget(
                modifier =
                    Modifier
                        .padding(horizontal = padding_screen)
                        .padding(top = padding_screen_small)
                        .fillMaxWidth(),
                text =
                    stringResource(
                        R.string.txt_total_amount,
                        totalAmount,
                    ),
                textAlign = TextAlign.End,
            )
        }
        repeat(models.size) {
            PaymentWidget(
                model = models[it],
                canDeletePayment = canDeletePayment,
                onDeletePayment = onDeletePayment,
            )
        }
    }
}

@AppPreview
@Composable
private fun PaymentPlanListScreenPreview() {
    AppThemeComposable {
        PaymentsListScreen(
            models =
                listOf(
                    getMockActivePaymentPresentationModel(),
                    getMockActivePaymentPresentationModel(),
                    getMockExpiredPaymentPresentationModel(),
                ),
            totalAmount = "GBP 12,000.02",
            canDeletePayment = true,
        )
    }
}

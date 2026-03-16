package com.ndemi.garden.gym.ui.widgets.payments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActivePaymentPresentationModel
import com.ndemi.garden.gym.ui.mock.getMockExpiredPaymentPresentationModel
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import cv.domain.presentationModels.PaymentPresentationModel

@Composable
fun PaymentWidget(
    modifier: Modifier = Modifier,
    model: PaymentPresentationModel,
    canDeletePayment: Boolean = false,
    onDeletePayment: (PaymentPresentationModel) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .padding(top = padding_screen_small)
                .padding(horizontal = padding_screen)
                .toAppCardStyle(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            TextWidget(
                modifier = Modifier.weight(1f),
                text = model.paymentPlanDuration,
                color = AppTheme.colors.primary,
                style = AppTheme.textStyles.regularBold,
            )
            if (canDeletePayment) {
                Icon(
                    modifier = Modifier.clickable { showDialog = !showDialog },
                    imageVector = Icons.Default.DeleteForever,
                    tint = AppTheme.colors.error,
                    contentDescription = stringResource(id = R.string.txt_delete),
                )
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextWidget(
                    text = "Start Date",
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = model.startDateDayMonthYear)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextWidget(
                    text = "End Date",
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = model.endDateDayMonthYear)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                TextWidget(
                    text = stringResource(R.string.txt_total),
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = model.amount)
            }
        }

        if (showDialog) {
            AlertDialogWidget(
                title = stringResource(R.string.txt_are_you_sure),
                message = stringResource(R.string.txt_are_you_sure_delete_payment),
                onDismissed = { showDialog = !showDialog },
                positiveButton = stringResource(R.string.txt_delete),
                positiveOnClick = {
                    showDialog = !showDialog
                    onDeletePayment.invoke(model)
                },
                negativeButton = stringResource(R.string.txt_cancel),
                negativeOnClick = {
                    showDialog = !showDialog
                },
            )
        }
    }
}

@AppPreview
@Composable
private fun PaymentWidgetPreview() =
    AppThemeComposable {
        Column {
            PaymentWidget(
                model = getMockActivePaymentPresentationModel(),
            )
            PaymentWidget(
                model = getMockExpiredPaymentPresentationModel(),
                canDeletePayment = true,
            )
            PaymentWidget(
                model = getMockExpiredPaymentPresentationModel(),
            )
        }
    }

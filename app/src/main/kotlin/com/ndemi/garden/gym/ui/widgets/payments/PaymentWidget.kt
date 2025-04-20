package com.ndemi.garden.gym.ui.widgets.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
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
import com.ndemi.garden.gym.ui.mock.getMockActivePaymentEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredPaymentEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.toAmountString
import com.ndemi.garden.gym.ui.utils.toPaymentPlanDuration
import com.ndemi.garden.gym.ui.widgets.AlertDialogWidget
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.PaymentEntity
import org.joda.time.DateTime

@Composable
fun PaymentWidget(
    modifier: Modifier = Modifier,
    paymentEntity: PaymentEntity,
    canDeletePayment: Boolean = false,
    onDeletePayment: (PaymentEntity) -> Unit = {},
) {
    val startDate = DateTime(paymentEntity.startDateMillis)
    val endDate = DateTime(paymentEntity.endDateMillis)
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .padding(horizontal = padding_screen)
                .padding(top = padding_screen_small)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(border_radius),
                ).border(
                    width = line_thickness,
                    color = AppTheme.colors.border,
                    shape = RoundedCornerShape(border_radius),
                ).padding(padding_screen_small),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextWidget(
                text = endDate.toPaymentPlanDuration(),
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
                    .padding(top = padding_screen_small)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                TextWidget(
                    text = "Start Date",
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = startDate.toString(formatDayMonthYear))
            }
            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextWidget(
                    text = "End Date",
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = endDate.toString(formatDayMonthYear))
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                TextWidget(
                    text = "Total",
                    style = AppTheme.textStyles.small,
                    color = AppTheme.colors.textSecondary,
                )
                TextWidget(text = paymentEntity.amount.toAmountString())
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
                    onDeletePayment.invoke(paymentEntity)
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
                paymentEntity = getMockActivePaymentEntity(),
            )
            PaymentWidget(
                paymentEntity = getMockExpiredPaymentEntity(),
                canDeletePayment = true,
            )
        }
    }

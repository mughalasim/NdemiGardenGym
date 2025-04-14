package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAddDetailsScreen(
    startDate: DateTime? = null,
    uiState: UiState,
    onSetData: (DateTime, String, String, InputType) -> Unit,
    onPaymentAddTapped: () -> Unit = {},
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var datePickerVisibility by remember { mutableStateOf(false) }
    var errorMonthDuration = ""
    var errorAmount = ""
    var errorStartDate = ""

    if (uiState is UiState.Error) {
        when(uiState.inputType){
            InputType.START_DATE -> errorStartDate = uiState.message
            InputType.MONTH_DURATION -> errorMonthDuration = uiState.message
            InputType.AMOUNT -> errorAmount = uiState.message
            else -> Unit
        }
    }

    TextWidget(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_desc)
    )

    TextWidget(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_select_date),
        style = AppTheme.textStyles.small,
    )

    ButtonWidget(
        title = startDate?.toString(formatDayMonthYear).orEmpty(),
    ) {
        datePickerVisibility = !datePickerVisibility
    }

    TextWidget(
        style = AppTheme.textStyles.small,
        color = AppTheme.colors.error,
        text = errorStartDate
    )

    TextWidget(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_select_duration),
        style = AppTheme.textStyles.small,
    )

    EditTextWidget(
        hint = stringResource(R.string.txt_payments_add_month_duration),
        errorText = errorMonthDuration,
        keyboardType = KeyboardType.Number
    ) {
        onSetData.invoke(DateTime.now(), it, it, InputType.MONTH_DURATION)
    }

    TextWidget(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_amount_paid),
        style = AppTheme.textStyles.small,
    )

    EditTextWidget(
        hint = stringResource(R.string.txt_amount),
        errorText = errorAmount,
        keyboardType = KeyboardType.Number
    ) {
        onSetData.invoke(DateTime.now(), it, it, InputType.AMOUNT)
    }

    ButtonWidget(
        title = stringResource(id = R.string.txt_update),
        isEnabled = uiState is UiState.Ready,
        isLoading = uiState is UiState.Loading
    ) {
        onPaymentAddTapped.invoke()
    }


    if (datePickerVisibility) {
        DatePickerDialog(
            onDismissRequest = {
                datePickerVisibility = !datePickerVisibility
            },
            confirmButton = {
                Text(
                    text = stringResource(R.string.txt_update),
                    style = AppTheme.textStyles.regularBold,
                    modifier = Modifier
                        .padding(padding_screen)
                        .clickable {
                            datePickerVisibility = !datePickerVisibility
                            state.selectedDateMillis?.let {
                                onSetData.invoke(DateTime(it), "", "", InputType.START_DATE)
                            }
                        }
                )
            },
            dismissButton = {
                Text(
                    text = stringResource(id = R.string.txt_cancel),
                    style = AppTheme.textStyles.regular,
                    modifier = Modifier
                        .padding(padding_screen)
                        .clickable {
                            datePickerVisibility = !datePickerVisibility
                        }
                )
            }
        ) {
            DatePicker(state = state, showModeToggle = false, headline = null, title = null)
        }
    }
}

@AppPreview
@Composable
private fun PaymentAddDetailsScreenPreview() {
    AppThemeComposable{
        Column(
            modifier = Modifier
                .padding(horizontal = padding_screen)
        ) {
            PaymentAddDetailsScreen(
                startDate = DateTime.now(),
                onSetData = {_,_,_,_ -> },
                uiState = UiState.Ready,
            )
        }
    }
}

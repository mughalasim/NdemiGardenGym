package com.ndemi.garden.gym.ui.screens.paymentadd

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.page_width
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.SnackbarType
import com.ndemi.garden.gym.ui.widgets.TextWidget
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAddDetailsScreen(
    inputData: PaymentAddScreenViewModel.InputData = PaymentAddScreenViewModel.InputData(),
    uiState: UiState = UiState.Ready,
    onSetData: (DateTime, String, String, InputType) -> Unit = { _, _, _, _ -> },
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    onPaymentAddTapped: () -> Unit = {},
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var datePickerVisibility by remember { mutableStateOf(false) }
    var errorMonthDuration = ""
    var errorAmount = ""
    var errorStartDate = ""

    if (uiState is UiState.Error) {
        when (uiState.inputType) {
            InputType.START_DATE -> errorStartDate = uiState.message
            InputType.MONTH_DURATION -> errorMonthDuration = uiState.message
            InputType.AMOUNT -> errorAmount = uiState.message
            InputType.NONE ->
                snackbarHostState.Show(
                    type = SnackbarType.ERROR,
                    message = uiState.message,
                )
        }
    }

    Column(
        modifier =
            Modifier
                .requiredWidth(page_width)
                .verticalScroll(rememberScrollState()),
    ) {
        TextWidget(
            style = AppTheme.textStyles.large,
            modifier = Modifier.padding(padding_screen),
            text = stringResource(R.string.txt_payments_add_desc),
        )

        Row(
            modifier = Modifier.padding(top = padding_screen_large),
        ) {
            EditTextWidget(
                modifier =
                    Modifier
                        .weight(DATE_WEIGHT)
                        .padding(end = padding_screen_small),
                canClear = false,
                isEnabled = false,
                errorText = errorStartDate,
                hint = stringResource(R.string.txt_payments_add_select_date),
                textInput = inputData.startDate.toString(formatDayMonthYear).orEmpty(),
            )
            ButtonWidget(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.txt_select),
                isOutlined = true,
            ) {
                datePickerVisibility = !datePickerVisibility
            }
        }

        EditTextWidget(
            modifier = Modifier.padding(top = padding_screen),
            hint = stringResource(R.string.txt_payments_add_select_duration),
            textInput = (inputData.monthDuration.takeIf { it != 0 } ?: "").toString(),
            errorText = errorMonthDuration,
            keyboardType = KeyboardType.Number,
        ) {
            onSetData.invoke(DateTime.now(), it, it, InputType.MONTH_DURATION)
        }

        EditTextWidget(
            modifier = Modifier.padding(top = padding_screen),
            hint = stringResource(R.string.txt_payments_add_amount_paid, BuildConfig.CURRENCY_CODE),
            textInput = (inputData.amount.takeIf { it != 0 } ?: "").toString(),
            errorText = errorAmount,
            keyboardType = KeyboardType.Number,
        ) {
            onSetData.invoke(DateTime.now(), it, it, InputType.AMOUNT)
        }

        ButtonWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen_large)
                    .fillMaxWidth(),
            title = stringResource(id = R.string.txt_update),
            isEnabled = uiState is UiState.Ready,
            isLoading = uiState is UiState.Loading,
        ) {
            onPaymentAddTapped.invoke()
        }
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
                    modifier =
                        Modifier
                            .padding(padding_screen)
                            .clickable {
                                datePickerVisibility = !datePickerVisibility
                                state.selectedDateMillis?.let {
                                    onSetData.invoke(DateTime(it), "", "", InputType.START_DATE)
                                }
                            },
                )
            },
            dismissButton = {
                Text(
                    text = stringResource(id = R.string.txt_cancel),
                    style = AppTheme.textStyles.regular,
                    modifier =
                        Modifier
                            .padding(padding_screen)
                            .clickable {
                                datePickerVisibility = !datePickerVisibility
                            },
                )
            },
        ) {
            DatePicker(state = state, showModeToggle = false, headline = null, title = null)
        }
    }
}

private const val DATE_WEIGHT = 3f

@AppPreview
@Composable
private fun PaymentAddDetailsScreenPreview() =
    AppThemeComposable {
        PaymentAddDetailsScreen()
    }

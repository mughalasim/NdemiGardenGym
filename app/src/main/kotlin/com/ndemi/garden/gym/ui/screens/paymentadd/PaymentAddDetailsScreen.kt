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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import kotlinx.coroutines.flow.MutableStateFlow
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAddDetailsScreen(
    startDate: DateTime? = null,
    uiState: State<UiState>,
    onSetData: (DateTime, String, String, InputType) -> Unit,
    onPaymentAddTapped: () -> Unit = {},
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var datePickerVisibility by remember { mutableStateOf(false) }

    TextRegular(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_desc)
    )

    TextSmall(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_select_date)
    )

    ButtonWidget(
        title = startDate?.toString(formatDayMonthYear).orEmpty(),
    ) {
        datePickerVisibility = !datePickerVisibility
    }


    TextSmall(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_select_duration)
    )

    EditTextWidget(
        hint = stringResource(R.string.txt_payments_add_month_duration),
        isError = (uiState.value as? UiState.Error)?.inputType == InputType.MONTH_DURATION,
        keyboardType = KeyboardType.Number
    ) {
        onSetData.invoke(DateTime.now(), it, it, InputType.MONTH_DURATION)
    }

    TextSmall(
        modifier = Modifier.padding(top = padding_screen),
        text = stringResource(R.string.txt_payments_add_amount_paid)
    )

    EditTextWidget(
        hint = stringResource(R.string.txt_amount),
        isError = (uiState.value as? UiState.Error)?.inputType == InputType.AMOUNT,
        keyboardType = KeyboardType.Number
    ) {
        onSetData.invoke(DateTime.now(), it, it, InputType.AMOUNT)
    }

    ButtonWidget(
        title = stringResource(id = R.string.txt_update),
        isEnabled = uiState.value is UiState.Ready,
        isLoading = uiState.value is UiState.Loading
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
fun PaymentAddDetailsScreenPreview() {
    AppThemeComposable{
        Column(
            modifier = Modifier
                .padding(horizontal = padding_screen)
        ) {
            PaymentAddDetailsScreen(
                startDate = DateTime.now(),
                onSetData = {_,_,_,_ -> },
                uiState = MutableStateFlow(UiState.Ready).collectAsState(
                    initial = UiState.Ready
                )
            )
        }
    }
}

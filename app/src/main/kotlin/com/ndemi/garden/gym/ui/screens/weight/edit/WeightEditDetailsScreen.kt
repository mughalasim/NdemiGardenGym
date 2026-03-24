package com.ndemi.garden.gym.ui.screens.weight.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
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
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget

@Composable
fun WeightEditDetailsScreen(
    listeners: WeightEditDetailsScreenListeners = WeightEditDetailsScreenListeners(),
    uiState: WeightEditScreenViewModel.WeightUiState = WeightEditScreenViewModel.WeightUiState(),
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var datePickerVisibility by remember { mutableStateOf(false) }

    Column {
        ToolBarWidget(
            title = if (uiState.isEditMode) stringResource(R.string.txt_edit_weight) else stringResource(R.string.txt_add_weight),
            canNavigateBack = true,
            onBackPressed = listeners.onBackTapped,
        )

        Column(modifier = Modifier.padding(padding_screen)) {
            TextWidget(text = stringResource(R.string.txt_capture_weight_desc, uiState.model.weightUnit))

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
                    errorText = uiState.errorDate,
                    hint = stringResource(R.string.txt_set_date),
                    textInput = uiState.model.formattedDate,
                )
                ButtonWidget(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.txt_select),
                    hideKeyboardOnClick = true,
                    isOutlined = true,
                ) {
                    datePickerVisibility = !datePickerVisibility
                }
            }

            EditTextWidget(
                modifier = Modifier.padding(top = padding_screen),
                textInput = uiState.model.weightValue,
                hint = stringResource(R.string.txt_weight, uiState.model.weightUnit),
                onValueChanged = listeners.onWeightValueChanged,
                errorText = uiState.errorWeight,
                keyboardType = KeyboardType.Number,
            )

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
                                            listeners.onDateSelected.invoke(it)
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

            ButtonWidget(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen),
                title = if (uiState.isEditMode) stringResource(R.string.txt_update) else stringResource(R.string.txt_add),
                isEnabled = uiState.updateEnabled,
                onButtonClicked = listeners.onAddTapped,
                hideKeyboardOnClick = true,
            )
        }
    }
}

data class WeightEditDetailsScreenListeners(
    val onBackTapped: () -> Unit = {},
    val onWeightValueChanged: (String) -> Unit = {},
    val onDateSelected: (Long) -> Unit = {},
    val onAddTapped: () -> Unit = {},
)

private const val DATE_WEIGHT = 2f

@AppPreview
@Composable
private fun WeightEditDetailsScreenPreview() =
    AppThemeComposable {
        WeightEditDetailsScreen()
    }

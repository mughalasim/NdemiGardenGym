package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.WeightWidget
import com.ndemi.garden.gym.ui.widgets.dialog.InputDialogWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackedWeightComponent(viewModel: WeightViewModel = koinViewModel<WeightViewModel>()) {
    val weightState by viewModel.weightState.collectAsStateWithLifecycle()
    val memberEntity by viewModel.memberEntity.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .padding(top = padding_screen)
                .toAppCardStyle(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextWidget(
                text = stringResource(R.string.txt_tracked_weight),
                style = AppTheme.textStyles.large,
            )
            ButtonWidget(
                title = stringResource(R.string.txt_add),
                overridePadding = padding_screen_small,
                isOutlined = true,
            ) {
                showDialog = !showDialog
            }
        }
        if (memberEntity.trackedWeights.isEmpty()) {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen),
                text = stringResource(R.string.txt_no_weight_recorded),
            )
        } else {
            for (weight in memberEntity.trackedWeights) {
                WeightWidget(weightEntity = weight, onDeleteWeight = viewModel::onDeleteWeightTapped)
            }
        }
    }

    if (showDialog) {
        InputDialogWidget(
            title = stringResource(R.string.txt_capture_weight),
            message = stringResource(R.string.txt_capture_weight_desc),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_update),
            positiveOnClick = {
                showDialog = !showDialog
                viewModel.onAddWeightTapped()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = { showDialog = !showDialog },
            editTextInput = weightState.inputText,
            editTextError = weightState.errorText,
            editTextKeyboardType = KeyboardType.Number,
            editTextValueChanged = viewModel::onWeightValueChanged,
        )
    }
}

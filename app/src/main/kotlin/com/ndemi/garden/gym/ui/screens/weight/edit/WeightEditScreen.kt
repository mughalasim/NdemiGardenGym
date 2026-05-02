package com.ndemi.garden.gym.ui.screens.weight.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeightEditScreen(viewModel: WeightEditScreenViewModel) {
    val weightState by viewModel.weightState.collectAsStateWithLifecycle()
    WeightEditDetailsScreen(
        listeners =
            WeightEditDetailsScreenListeners(
                onBackTapped = viewModel::onBackTapped,
                onWeightValueChanged = viewModel::onWeightValueChanged,
                onDateSelected = viewModel::onDateSelected,
                onAddTapped = viewModel::onAddTapped,
            ),
        uiState = weightState,
    )
}

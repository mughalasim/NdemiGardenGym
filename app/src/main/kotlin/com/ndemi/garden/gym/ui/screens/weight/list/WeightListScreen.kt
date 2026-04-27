package com.ndemi.garden.gym.ui.screens.weight.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeightListScreen(viewModel: WeightListScreenViewModel) {
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val weightList by viewModel.weightList.collectAsStateWithLifecycle()
    val weightChange by viewModel.weightChange.collectAsStateWithLifecycle()

    WeightListDetailsScreen(
        selectedYear = selectedYear,
        weightList = weightList,
        weightChange = weightChange,
        listeners =
            WeightListDetailsScreenListeners(
                onBackTapped = viewModel::navigateBack,
                onDeleteTapped = viewModel::deleteWeight,
                onEditTapped = viewModel::editWeight,
                onAddTapped = viewModel::addWeight,
                onYearMinusTapped = viewModel::decreaseYear,
                onYearPlusTapped = viewModel::increaseYear,
            ),
    )
}

package com.ndemi.garden.gym.ui.screens.weight.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.weight.list.WeightListScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeightListScreen(
    viewModel: WeightListScreenViewModel = koinViewModel<WeightListScreenViewModel>(),
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val weightList by viewModel.weightList.collectAsStateWithLifecycle()
    val weightChange by viewModel.weightChange.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Success -> {
            snackbarHostState.Show(
                type = SnackbarType.SUCCESS,
                message = state.message,
            )
        }

        is UiState.Error -> {
            snackbarHostState.Show(
                type = SnackbarType.ERROR,
                message = state.message,
            )
        }

        else -> {
            Unit
        }
    }

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

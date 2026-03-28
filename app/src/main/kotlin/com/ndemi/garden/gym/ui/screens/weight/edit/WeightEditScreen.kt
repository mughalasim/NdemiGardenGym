package com.ndemi.garden.gym.ui.screens.weight.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.weight.edit.WeightEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WeightEditScreen(
    weightId: String = "",
    weight: String = "",
    dateMillis: Long = 0L,
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    viewModel: WeightEditScreenViewModel =
        koinViewModel<WeightEditScreenViewModel>(parameters = { parametersOf(weightId, weight, dateMillis) }),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val weightState by viewModel.weightState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Error -> {
            snackbarHostState.Show(
                type = SnackbarType.ERROR,
                message = state.message,
            )
        }

        else -> {}
    }

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

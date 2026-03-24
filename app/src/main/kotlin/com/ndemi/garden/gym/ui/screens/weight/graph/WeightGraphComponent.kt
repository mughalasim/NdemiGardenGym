package com.ndemi.garden.gym.ui.screens.weight.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeightComponent(viewModel: WeightGraphComponentViewModel = koinViewModel<WeightGraphComponentViewModel>()) {
    val weightDataList by viewModel.weightDataList.collectAsState()

    WeightGraphDetailsComponent(
        weightDataList = weightDataList,
        onAddWeightTapped = viewModel::onAddWeightTapped,
    )
}

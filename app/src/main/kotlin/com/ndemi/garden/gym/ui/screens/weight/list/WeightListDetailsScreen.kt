package com.ndemi.garden.gym.ui.screens.weight.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockWeightPresentationModel
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.DateSelectionWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WeightWidget
import cv.domain.presentationModels.WeightPresentationModel

@Composable
fun WeightListDetailsScreen(
    listeners: WeightListDetailsScreenListeners = WeightListDetailsScreenListeners(),
    selectedYear: Int = 0,
    weightChange: String,
    weightList: List<WeightPresentationModel> = emptyList(),
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        ToolBarWidget(
            title = stringResource(R.string.txt_tracked_weight),
            canNavigateBack = true,
            onBackPressed = listeners.onBackTapped,
            secondaryIcon = Icons.Default.AddCircle,
            onSecondaryIconPressed = listeners.onAddTapped,
        )

        DateSelectionWidget(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding_screen),
            selectedText = selectedYear.toString(),
            onMinusTapped = listeners.onYearMinusTapped,
            onPlusTapped = listeners.onYearPlusTapped,
        )
        if (weightChange.isNotEmpty()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding_screen)
                        .padding(horizontal = padding_screen),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextWidget(text = stringResource(R.string.txt_total_weight_change))
                TextWidget(
                    modifier = Modifier.padding(horizontal = padding_screen_small),
                    text = weightChange,
                )
            }
        }

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = padding_screen)
                    .verticalScroll(rememberScrollState()),
        ) {
            if (weightList.isEmpty()) {
                TextWidget(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(padding_screen),
                    text = stringResource(R.string.txt_no_weight_recorded),
                    textAlign = TextAlign.Center,
                )
            }
            for (weight in weightList) {
                WeightWidget(
                    weight = weight,
                    onDeleteWeight = listeners.onDeleteTapped,
                    onEditWeight = listeners.onEditTapped,
                )
            }
        }
    }
}

data class WeightListDetailsScreenListeners(
    val onBackTapped: () -> Unit = {},
    val onDeleteTapped: (String) -> Unit = {},
    val onEditTapped: (WeightPresentationModel) -> Unit = {},
    val onAddTapped: () -> Unit = {},
    val onYearMinusTapped: () -> Unit = {},
    val onYearPlusTapped: () -> Unit = {},
)

@AppPreview
@Composable
private fun WeightListDetailsScreenPreview() {
    AppThemeComposable {
        WeightListDetailsScreen(
            selectedYear = 2023,
            weightChange = "-3.6 Kg",
            weightList =
                listOf(
                    getMockWeightPresentationModel(),
                    getMockWeightPresentationModel(),
                    getMockWeightPresentationModel(),
                ),
        )
    }
}

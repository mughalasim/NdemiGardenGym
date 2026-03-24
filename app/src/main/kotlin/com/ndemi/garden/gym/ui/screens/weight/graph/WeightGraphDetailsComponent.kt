package com.ndemi.garden.gym.ui.screens.weight.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun WeightGraphDetailsComponent(
    weightDataList: List<WeightData> = emptyList(),
    onAddWeightTapped: () -> Unit = {},
) {
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
                onButtonClicked = onAddWeightTapped,
            )
        }
        val chartTextColor = AppTheme.colors.textPrimary.toArgb()
        val chartHighlightColor = AppTheme.colors.primary.toArgb()

        val chartColors =
            remember(chartTextColor, chartHighlightColor) {
                ChartColors(
                    textColor = chartTextColor,
                    highlightColor = chartHighlightColor,
                    alpha = 100,
                )
            }

        val lineDataWeight =
            remember(weightDataList) {
                weightDataList.createDataSetWithColor(
                    label = "Tracked weight",
                    chartColors = chartColors,
                )
            }

        val chartModifier =
            if (weightDataList.isNotEmpty()) {
                Modifier.fillMaxWidth().height(CHART_HEIGHT_EXPANDED)
            } else {
                Modifier.fillMaxWidth().wrapContentHeight()
            }

        AndroidView(
            modifier = chartModifier,
            factory = { context ->
                LineChart(context).apply {
                    setupLineChart(
                        chartColors = chartColors,
                        noDataText = "No tracked weight this year",
                        descriptionText = "Tracked weight this year",
                    )
                    if (weightDataList.isNotEmpty()) {
                        this.data = LineData(lineDataWeight)
                        this.notifyDataSetChanged()
                    }
                }
            },
            update = { view ->
                if (weightDataList.isNotEmpty()) {
                    view.data = LineData(lineDataWeight)
                    view.notifyDataSetChanged()
                    view.invalidate()
                }
            },
        )
    }
}

private val CHART_HEIGHT_EXPANDED = 200.dp

@AppPreview
@Composable
private fun WeightGraphDetailsComponentPreview() =
    AppThemeComposable {
        WeightGraphDetailsComponent()
    }

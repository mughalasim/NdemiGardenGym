package com.ndemi.garden.gym.ui.screens.weight.graph

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

fun LineChart.setupLineChart(
    chartColors: ChartColors,
    noDataText: String,
    descriptionText: String,
): LineChart =
    this.apply {
        setTouchEnabled(true)
        isDragEnabled = true
        axisRight.isEnabled = false
        setScaleEnabled(true)
        setPinchZoom(true)
        setNoDataTextColor(chartColors.textColor)
        setNoDataText(noDataText)

        description.apply {
            isEnabled = true
            text = descriptionText
            textColor = chartColors.textColor
        }

        legend.apply {
            isEnabled = true
            textColor = chartColors.textColor
        }

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = chartColors.textColor
        }

        axisLeft.apply {
            setDrawGridLines(false)
            textColor = chartColors.textColor
        }
    }

fun List<WeightData>.createDataSetWithColor(
    label: String,
    chartColors: ChartColors,
): LineDataSet {
    val entries =
        this.map { value ->
            Entry(value.date, value.weight)
        }
    return LineDataSet(entries, label).apply {
        color = chartColors.highlightColor
        setCircleColor(chartColors.highlightColor)
        setDrawFilled(true)
        setDrawCircles(true)
        valueTextColor = chartColors.textColor
        fillAlpha = chartColors.alpha
        fillColor = chartColors.highlightColor
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
}

data class ChartColors(
    val textColor: Int,
    val highlightColor: Int,
    val alpha: Int,
)

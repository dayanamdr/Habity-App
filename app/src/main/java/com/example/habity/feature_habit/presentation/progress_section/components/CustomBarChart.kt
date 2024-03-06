package com.example.habity.feature_habit.presentation.progress_section.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.axis.Gravity
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType

@Composable
fun CustomBarChart() {
    val stepSize = 10 // replace with the maximum number of habits that week
    val barChartListSize = 7
    val maxRange = 10 // replace with the maximum number of habits that week
    val barData = DataUtils.getBarChartData(
        listSize = barChartListSize,
        maxRange = maxRange,
        barChartType = BarChartType.VERTICAL,
        dataCategoryOptions = DataCategoryOptions()
    )

    val weekDays: List<String> =
        listOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")

    val xAxisData = AxisData.Builder()
        .axisStepSize(80.dp)
        .steps(barData.size - 1)
        .bottomPadding(10.dp)
        .axisLabelAngle(20f)
        .labelAndAxisLinePadding(10.dp)
        .startDrawPadding(25.dp)
        .endPadding(25.dp)
        .labelData { index -> weekDays[index] }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(stepSize)
        .labelAndAxisLinePadding(5.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / stepSize)).toString() }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surface
    )

    BarChart(modifier = Modifier.height(350.dp), barChartData = barChartData)
}
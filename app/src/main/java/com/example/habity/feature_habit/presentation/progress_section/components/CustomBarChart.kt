package com.example.habity.feature_habit.presentation.progress_section.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData

@Composable
fun CustomBarChart(weeklyCompletedHabits: List<Int>) {
    val stepSize = 10
    val maxRange = 10
    val chartDataList = mutableListOf<BarData>()
    val weeklyHabitsCompleted = weeklyCompletedHabits.ifEmpty { MutableList(7) { 0 } }
    val weekDays: List<String> = listOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")

    weeklyHabitsCompleted.forEachIndexed { index, countCompletedHabits ->
        val point = Point(x = index.toFloat(), y = countCompletedHabits.toFloat())
        chartDataList.add(BarData(point = point, color = Color(0xff03a9f4)))
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(80.dp)
        .steps(chartDataList.size - 1)
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
        chartData = chartDataList,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surface
    )

    BarChart(modifier = Modifier.height(350.dp), barChartData = barChartData)
}
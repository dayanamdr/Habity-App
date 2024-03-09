package com.example.habity.feature_habit.presentation.progress_section.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.habity.feature_habit.domain.model.Habit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomDonutChart(habits: List<Habit>) {
    val colors = listOf(0xff9c27b0,0xffe91e63,0xff8bc34a,0xffcddc39,
        0xff3f51b5,0xff2196f3,0xff03a9f4,0xff00bcd4,
        0xff009688,0xff673ab7, 0xfff44336,0xff4caf50,
        0xffffeb3b,0xffffc107,0xffff9800,0xffff5722,
        0xff795548,0xff9e9e9e,0xff607d8b,0xff333333
    )
    val grayColor = 0xffb0b0b0
    val habitSlices = mutableListOf<PieChartData.Slice>()

    if (habits.isEmpty()) {
        habitSlices.add(PieChartData.Slice(
            "In progress",
            100f,
            Color(grayColor))
        )
    } else {
        val sliceSize: Float = (100 / habits.size).toFloat()
        habits.forEachIndexed { index, habit ->
            val sliceColor = if (habit.completed) colors[index] else grayColor
            habitSlices.add(PieChartData.Slice(habit.label, sliceSize, Color(sliceColor)))
        }
    }

    val donutChartData = PieChartData(
        slices = habitSlices,
        plotType = PlotType.Donut
    )

    val donutChartConfig = PieChartConfig(
        strokeWidth = 120f,
        isAnimationEnable = true,
        animationDuration = 600,
        showSliceLabels = true,
        chartPadding = 20,
        isClickOnSliceEnabled = false,
        labelVisible = true
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DonutPieChart(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp),
                donutChartData,
                donutChartConfig
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                pieChartData = donutChartData,
                gridSize = 3)
            )
        }
    }


}
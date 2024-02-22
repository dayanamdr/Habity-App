package com.example.habity.feature_habit.presentation.home_section.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    mealOrder: HabitOrder = HabitOrder.Date(OrderType.Descending),
    onChangeOrder: (HabitOrder) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Name",
                selected = mealOrder is HabitOrder.Name ,
                onSelect = { onChangeOrder(HabitOrder.Name(mealOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Label",
                selected = mealOrder is HabitOrder.Label ,
                onSelect = { onChangeOrder(HabitOrder.Label(mealOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = mealOrder is HabitOrder.Date ,
                onSelect = { onChangeOrder(HabitOrder.Date(mealOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = mealOrder.orderType is OrderType.Ascending ,
                onSelect = { onChangeOrder(mealOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = mealOrder.orderType is OrderType.Descending ,
                onSelect = { onChangeOrder(mealOrder.copy(OrderType.Descending)) }
            )
        }
    }
}
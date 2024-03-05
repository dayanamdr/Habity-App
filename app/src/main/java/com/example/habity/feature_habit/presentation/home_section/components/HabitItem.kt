package com.example.habity.feature_habit.presentation.home_section.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habity.feature_habit.domain.model.Habit

@Composable
fun HabitItem(
    habit: Habit,
    onCheckBoxChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checked = remember { mutableStateOf(habit.completed) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = habit.name, style = MaterialTheme.typography.headlineMedium)
                Checkbox(
                    checked = checked.value,
                    onCheckedChange = {
                            isChecked -> checked.value = isChecked
                            onCheckBoxChange()
                    }
                )
            }
            Divider(thickness = 1.dp, color = Color.LightGray)
            Text(
                text = habit.label,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
//            Text(text = "Id: ${habit.id}", style = MaterialTheme.typography.bodyLarge)
//            Text(text = "localId: ${habit.idLocal}", style = MaterialTheme.typography.bodyLarge)
            Text(text = habit.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = habit.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
            }

        }
    }
}
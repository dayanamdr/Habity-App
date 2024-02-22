package com.example.habity.feature_habit.presentation.home_section.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habity.feature_habit.domain.model.Habit

@Composable
fun HabitItem(
    habit: Habit,
    modifier: Modifier = Modifier
) {
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
                Text(text = habit.label, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.padding(top = 6.dp))
            Text(text = "Description: ${habit.description}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Date: ${habit.date} g", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
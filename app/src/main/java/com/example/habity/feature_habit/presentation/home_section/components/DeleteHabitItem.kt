package com.example.habity.feature_habit.presentation.home_section.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.habity.feature_habit.domain.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteHabitItem(
    modifier: Modifier = Modifier,
    habit: Habit,
    onDelete: () -> Unit,
    onClickMealItem: () -> Unit,
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            it == DismissValue.DismissedToStart
        }, positionalThreshold = { 150.dp.toPx() }
    )
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var show by remember { mutableStateOf(true) }

    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = { DismissBackground(dismissState) },
            dismissContent = {
                HabitItem(
                    habit = habit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClickMealItem()
                        }
                )
            }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    LaunchedEffect(key1 = dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToStart) {
            showDialog = true
            dismissState.reset()
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            onConfirmation = {
                onDelete()
                showDialog = false
                show = false
                Toast.makeText(context, "Habit removed", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = {
                showDialog = false
            },
            dialogTitle = "Are you sure you want to delete it?",
            dialogText = "Delete habit",
        )
    }
}
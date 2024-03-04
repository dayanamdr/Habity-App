package com.example.habity.feature_habit.presentation.home_section

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habity.feature_habit.presentation.home_section.components.DeleteHabitItem
import com.example.habity.feature_habit.presentation.home_section.components.OrderSection
import com.example.habity.feature_habit.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.value

    val showNewHabitEntityDialog by viewModel.showNewHabitEntityDialog
    val newHabitEntity by viewModel.newHabitEntity

    if (showNewHabitEntityDialog && newHabitEntity != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissNewHabitEntityDialog()
            },
            title = {
                Text("New Habit entity has been created")
            },
            text = {
                Column {
                    Text("Name: ${newHabitEntity!!.name}")
                    Text("Description: ${newHabitEntity!!.description}")
                    Text("Label: ${newHabitEntity!!.label}")
                    Text("Date: ${newHabitEntity!!.date}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissNewHabitEntityDialog()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is HomeViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 55.dp),
                onClick = { navController.navigate(Screen.AddEditScreen.route) },
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your habits", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { viewModel.onEvent(HabitsEvent.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    mealOrder = state.habitOrder,
                    onChangeOrder = {
                        viewModel.onEvent(HabitsEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(bottom = 45.dp),
                state = rememberLazyListState()
            ) {
                itemsIndexed(items = state.habits, key = {_, listItem -> listItem.hashCode()}) {
                        _, habit ->
                    DeleteHabitItem(
                        habit = habit,
                        onDelete = { viewModel.onEvent(HabitsEvent.DeleteHabit(habit)) },
                        onClickMealItem = {
                            navController.navigate(Screen.AddEditScreen.route +
                                    "?habitId=${habit.idLocal}"
                            )}
                    )
                }
            }
        }

    }
}
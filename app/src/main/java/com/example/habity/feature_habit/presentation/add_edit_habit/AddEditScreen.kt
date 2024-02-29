package com.example.habity.feature_habit.presentation.add_edit_habit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habity.R
import com.example.habity.feature_habit.presentation.add_edit_habit.components.AddEditScreenTitle
import com.example.habity.feature_habit.presentation.add_edit_habit.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: AddEditMealViewModel = hiltViewModel()
) {
    val nameState = viewModel.habitName.value
    val labelState = viewModel.habitLabel.value
    val descriptionState = viewModel.habitDescription.value
    val dateState = viewModel.habitDate.value
    val habitId = viewModel.currentHabitId

    val titleText = if (habitId == null) stringResource(R.string.add_habit_title)
    else stringResource(R.string.edit_habit_title)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditMealViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditMealViewModel.UiEvent.SaveHabit -> {
                    navController.navigateUp()
                }
                is AddEditMealViewModel.UiEvent.CancelSaveHabit -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AddEditScreenTitle(titleText = titleText)

            Spacer(Modifier.height(16.dp))
            TransparentHintTextField(
                text = nameState.text,
                hint = nameState.hint,
                label = "Name",
                onValueChange = {
                    viewModel.onEvent(AddEditHabitEvent.EnteredName(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditHabitEvent.ChangeNameFocus(it))
                },
                isHintVisible = nameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            TransparentHintTextField(
                text = labelState.text,
                hint = labelState.hint,
                label = "Label",
                onValueChange = {
                    viewModel.onEvent(AddEditHabitEvent.EnteredLabel(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditHabitEvent.ChangeLabelFocus(it))
                },
                isHintVisible = labelState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
            )
            Spacer(Modifier.height(16.dp))
            TransparentHintTextField(
                text = descriptionState.text,
                hint = descriptionState.hint,
                label = "Description",
                onValueChange = {
                    viewModel.onEvent(AddEditHabitEvent.EnteredDescription(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditHabitEvent.ChangeDescriptionFocus(it))
                },
                isHintVisible = descriptionState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
            )
            Spacer(Modifier.height(16.dp))
            TransparentHintTextField(
                text = dateState.text,
                hint = dateState.hint,
                label = "Date",
                onValueChange = {
                    viewModel.onEvent(AddEditHabitEvent.EnteredDate(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditHabitEvent.ChangeDateFocus(it))
                },
                isHintVisible = dateState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
            )
            Spacer(Modifier.height(30.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onEvent(AddEditHabitEvent.SaveHabit) }
            ) {
                Text("Save")
            }
            Spacer(Modifier.height(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onEvent(AddEditHabitEvent.CancelSaveHabit) }
            ) {
                Text("Cancel")
            }
        }
    }
}
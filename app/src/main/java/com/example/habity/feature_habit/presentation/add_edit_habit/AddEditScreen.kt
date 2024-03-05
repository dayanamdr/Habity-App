package com.example.habity.feature_habit.presentation.add_edit_habit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habity.R
import com.example.habity.feature_habit.presentation.add_edit_habit.components.AddEditScreenTitle
import com.example.habity.feature_habit.presentation.add_edit_habit.components.CustomDatePickerDialog
import com.example.habity.feature_habit.presentation.add_edit_habit.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: AddEditMealViewModel = hiltViewModel()
) {
    val datePickerDate = remember { mutableStateOf(LocalDate.now())}
    val isDatePickerOpen = remember { mutableStateOf(false)}

    val nameState = viewModel.habitName.value
    val labelState = viewModel.habitLabel.value
    val descriptionState = viewModel.habitDescription.value
    val dateState = if (viewModel.habitDate.value.text == "") {
        viewModel.habitDate.value.copy(
            text = datePickerDate.value.format(DateTimeFormatter.ISO_DATE),
            isHintVisible = false
        )
    } else viewModel.habitDate.value
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

    if (isDatePickerOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                isDatePickerOpen.value = false // close dialog
                if (it != null) { // set the date
                    viewModel.onEvent(AddEditHabitEvent.EnteredDate(Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate().toString()))
                }
            },
            onCancel = {
                isDatePickerOpen.value = false // close dialog
            }
        )
    } else {
        // Set the current date as EnteredDate when no date is selected
        viewModel.onEvent(AddEditHabitEvent.EnteredDate(dateState.text))
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    readOnly = true,
                    value = dateState.text,
                    label = { Text("Date") },
                    onValueChange = { },
                    modifier = Modifier
                        .width(330.dp)
                )
                IconButton(
                    onClick = { isDatePickerOpen.value = true } // show the dialog
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendar"
                    )
                }
            }
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
package com.example.habity.feature_habit.domain.use_case

data class UseCases(
    val getHabitsUseCase: GetHabitsUseCase,
    val getHabitUseCase: GetHabitUseCase,
    val addEditUseCase: AddEditUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase
) {
}

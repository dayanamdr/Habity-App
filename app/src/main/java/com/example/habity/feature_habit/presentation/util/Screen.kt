package com.example.habity.feature_habit.presentation.util

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object AddEditScreen: Screen("add_edit_screen")
    object ProgressScreen: Screen("progress_screen")
    object ProfileScreen: Screen("profile_screen")
}

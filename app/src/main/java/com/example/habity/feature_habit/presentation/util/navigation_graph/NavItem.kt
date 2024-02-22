package com.example.habity.feature_habit.presentation.util.navigation_graph

sealed class NavItem(var title: String, var screenRoute: String) {
    object Add : NavItem("AddEdit", "add_edit_screen")
}

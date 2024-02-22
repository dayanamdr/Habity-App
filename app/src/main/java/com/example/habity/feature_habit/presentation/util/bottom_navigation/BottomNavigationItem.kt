package com.example.habity.feature_habit.presentation.util.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AreaChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(
    var title: String,
    var screenRoute: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector
) {
    object HomeScreen : BottomNavigationItem("Home", "home_screen", Icons.Filled.Home, Icons.Outlined.Home)
    object ProgressScreen : BottomNavigationItem("Progress", "progress_screen", Icons.Filled.AreaChart, Icons.Outlined.AreaChart)
    object ProfileScreen : BottomNavigationItem("Profile", "profile_screen", Icons.Filled.Person, Icons.Outlined.Person)
}

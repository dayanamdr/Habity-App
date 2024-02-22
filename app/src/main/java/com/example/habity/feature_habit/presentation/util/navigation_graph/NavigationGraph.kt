package com.example.habity.feature_habit.presentation.util.navigation_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.habity.feature_habit.presentation.add_edit_habit.AddEditScreen
import com.example.habity.feature_habit.presentation.home_section.MainScreen
import com.example.habity.feature_habit.presentation.profile_section.ProfileScreen
import com.example.habity.feature_habit.presentation.progress_section.ProgressScreen
import com.example.habity.feature_habit.presentation.util.bottom_navigation.BottomNavigationItem

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(navController, startDestination = BottomNavigationItem.HomeScreen.screenRoute) {
        composable(BottomNavigationItem.HomeScreen.screenRoute) {
            MainScreen(navController = navController)
        }
        composable(BottomNavigationItem.ProgressScreen.screenRoute) {
            ProgressScreen(navController = navController)
        }
        composable(BottomNavigationItem.ProfileScreen.screenRoute) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = NavItem.Add.screenRoute + "?habitId={habitId}",
            arguments = listOf(
                navArgument(
                    name = "habitId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditScreen(navController = navController)
        }
    }
}
package com.example.habity.feature_habit.presentation.util.bottom_navigation

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Addchart
import androidx.compose.material.icons.outlined.AreaChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habity.R

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.HomeScreen,
        BottomNavigationItem.ProgressScreen,
        BottomNavigationItem.ProfileScreen
    )

    androidx.compose.material.BottomNavigation(
        modifier = Modifier
            .height(60.dp),
        backgroundColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.black)
    ) {
        val selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEachIndexed { index, bottomNavItem ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            bottomNavItem.selectedIcon
                        } else bottomNavItem.unselectedIcon,
                        contentDescription = bottomNavItem.title
                    )
                },
                label = {
                    Text(
                        text = bottomNavItem.title,
                        fontSize = 16.sp
                    )
                },
                selectedContentColor = colorResource(id = R.color.teal_700),
                unselectedContentColor = colorResource(id = R.color.black).copy(0.4f),
                selected = currentRoute == bottomNavItem.screenRoute,
                onClick = {
                    navController.navigate(bottomNavItem.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
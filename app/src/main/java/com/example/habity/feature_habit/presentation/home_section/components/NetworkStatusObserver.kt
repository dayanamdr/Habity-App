package com.example.habity.feature_habit.presentation.home_section.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.habity.feature_habit.data.network.NetworkStatusChecker

@Composable
fun NetworkStatusObserver(snackbarHostState: SnackbarHostState, networkStatusChecker: NetworkStatusChecker) {
    val networkStatus by networkStatusChecker.networkStatus.collectAsState(initial = NetworkStatusChecker.NetworkStatus.Unavailable)

    LaunchedEffect(networkStatus) {
        when (networkStatus) {
            NetworkStatusChecker.NetworkStatus.Unavailable -> {
                snackbarHostState.showSnackbar(
                    message = "Unavailable network connection",
                    duration = SnackbarDuration.Short
                )
            }
            NetworkStatusChecker.NetworkStatus.Available -> {
                snackbarHostState.showSnackbar(
                    message = "Available network connection",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}
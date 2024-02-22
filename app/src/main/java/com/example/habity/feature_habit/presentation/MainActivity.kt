package com.example.habity.feature_habit.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.habity.feature_habit.presentation.util.bottom_navigation.BottomNavigation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.habity.R
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.presentation.home_section.components.NetworkStatusObserver
import com.example.habity.feature_habit.presentation.util.navigation_graph.NavigationGraph
import com.example.habity.ui.theme.HabityTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkStatusChecker: NetworkStatusChecker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatusChecker = NetworkStatusChecker(applicationContext)

        setContent {
            HabityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenView(networkStatusChecker = networkStatusChecker)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStatusChecker.stop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenView(networkStatusChecker: NetworkStatusChecker) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { BottomNavigation(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        NetworkStatusObserver(snackbarHostState = snackbarHostState, networkStatusChecker = networkStatusChecker)
        SetTitle()
        NavigationGraph(navController = navController)
    }
}

@Composable
fun SetTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 25.dp, top = 25.dp)
    ) {
        Column(
            modifier = modifier
        )
        {
            Text(
                text = "Habity",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.purple_700),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    HabityTheme {
//        Greeting("Android")
//    }
//}
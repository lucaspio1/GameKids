package com.pio.gamekids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pio.gamekids.ui.screens.CameraScreen
import com.pio.gamekids.ui.screens.DashboardScreen
import com.pio.gamekids.ui.theme.GameKidsTheme
import com.pio.gamekids.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameKidsTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        val vm: DashboardViewModel = viewModel()
                        DashboardScreen(vm, onTaskClick = { id -> navController.navigate("camera/$id") })
                    }
                    composable("camera/{taskId}") {
                        CameraScreen()
                    }
                }
            }
        }
    }
}
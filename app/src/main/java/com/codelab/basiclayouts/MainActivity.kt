package com.codelab.basiclayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codelab.basiclayouts.ui.screens.AddTransactionScreen
import com.codelab.basiclayouts.ui.screens.HistoryScreen
import com.codelab.basiclayouts.ui.screens.HomeScreen
import com.codelab.basiclayouts.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable(
                    route = "home"
                ) {
                    HomeScreen(
                        navigateToSettings = { navController.navigate("settings") },
                        navigateToAddTransaction = { navController.navigate("add_transaction") },
                        navigateToHistory = { navController.navigate("history") },
                    )
                }
                composable(
                    route = "settings",
                    enterTransition = { slideInVertically(initialOffsetY = {it}) },
                    exitTransition = { slideOutVertically (targetOffsetY = {it}) }
                ) {
                        SettingsScreen()
                }
                composable(
                    route = "add_transaction"
                ) {
                    AddTransactionScreen()
                }
                composable(
                    route = "history"

                ){
                    HistoryScreen()
                }
            }
        }
    }

}





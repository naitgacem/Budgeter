package com.codelab.basiclayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codelab.basiclayouts.ui.screens.AddTransactionScreen
import com.codelab.basiclayouts.ui.screens.HomeScreen
import com.codelab.basiclayouts.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            fun navigateToSettings() {
                navController.navigate("settings")
            }

            fun navigateToAddTransaction() {
                navController.navigate("add_transaction")
            }


            val LocalNavigateToSettings = staticCompositionLocalOf {
                navigateToAddTransaction()
            }


            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        navigateToSettings = { navigateToSettings() },
                        navigateToAddTransaction = { navigateToAddTransaction() }
                    )
                }
                composable("settings") { SettingsScreen() }
                composable("add_transaction") {
                    AddTransactionScreen(
                    )
                }
            }
        }
    }

}





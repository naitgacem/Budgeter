package com.aitgacem.budgeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.screens.DepositScreen
import com.aitgacem.budgeter.ui.screens.HomeScreen
import com.aitgacem.budgeter.ui.screens.SettingsScreen
import com.aitgacem.budgeter.ui.screens.TransactionDetailsScreen
import com.aitgacem.budgeter.ui.screens.WithdrawScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
            ) {
                composable(
                    route = Screen.Home.route,
                    enterTransition = { fadeIn() },
                ) {
                    HomeScreen(topLevelNavHost = navController)
                }

                composable(
                    route = Screen.TransactionDetails.route,
                    enterTransition = { scaleIn() },
                ) { backStackEntry ->
                    TransactionDetailsScreen(
                        id = backStackEntry.arguments?.getString("id") ?: "",
                        navController = navController
                    )
                }

                composable(
                    route = Screen.Settings.route,
                    enterTransition = { slideInHorizontally { it } },
                    exitTransition = { slideOutHorizontally { it } },
                ) { SettingsScreen(navController = navController) }


                composable(
                    enterTransition = { slideInHorizontally { it } },
                    exitTransition = { slideOutHorizontally { it } },
                    route = Screen.Deposit.route,
                    arguments = listOf(
                        navArgument("id") {
                            nullable = true
                        }
                    )
                ) { backStackEntry ->
                    DepositScreen(
                        navHostController = navController,
                        id = backStackEntry.arguments?.getString("id"),
                    )
                }

                composable(
                    enterTransition = { slideInHorizontally { it } },
                    exitTransition = { slideOutHorizontally { it } },
                    route = Screen.Withdraw.route,
                ) { WithdrawScreen(navHostController = navController) }


            }
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

fun NavController.navigateOnce(route: String) {
    if (this.currentBackStackEntry?.lifecycleIsResumed() == true) {
        this.navigate(route)
    }
}

fun NavController.popOnce() {
    if (this.currentBackStackEntry?.lifecycleIsResumed() == true) {
        this.popBackStack()
    }
}





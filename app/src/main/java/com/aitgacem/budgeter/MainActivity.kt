package com.aitgacem.budgeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.components.navBarVisibleIn
import com.aitgacem.budgeter.ui.screens.AnalyticsScreen
import com.aitgacem.budgeter.ui.screens.DepositScreen
import com.aitgacem.budgeter.ui.screens.GoalsScreen
import com.aitgacem.budgeter.ui.screens.OverviewScreen
import com.aitgacem.budgeter.ui.screens.SettingsScreen
import com.aitgacem.budgeter.ui.screens.TransactionDetailsScreen
import com.aitgacem.budgeter.ui.screens.TransactionsScreen
import com.aitgacem.budgeter.ui.screens.WithdrawScreen
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentScreen by navController.currentBackStackEntryAsState()
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = navBarVisibleIn[currentScreen?.destination?.route] == true,
                        enter = slideInVertically { it },
                    ) {
                        NavBar(navController = navController)
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Overview.route,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    composable(
                        route = Screen.Overview.route,
                        enterTransition = { fadeIn() },
                    ) {
                        OverviewScreen(
                            overviewNavController = navController,
                        )
                    }

                    composable(
                        route = Screen.Transactions.route,
                        enterTransition = { fadeIn() },
                    ) { TransactionsScreen(navController = navController) }

                    composable(
                        route = Screen.Settings.route,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                    ) { SettingsScreen(navController = navController) }

                    composable(
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        route = Screen.Withdraw.route,
                    ) { WithdrawScreen(navHostController = navController) }

                    composable(
                        route = Screen.TransactionDetails.route,
                        enterTransition = { scaleIn() },
                    ) { backStackEntry ->
                        TransactionDetailsScreen(
                            id = backStackEntry.arguments?.getString("id") ?: "",
                            navController = navController
                        )
                    }

                    composable(route = Screen.Analytics.route) {
                        AnalyticsScreen(navController = navController)
                    }

                    composable(route = Screen.Goals.route) {
                        GoalsScreen(navController = navController)
                    }

                    composable(
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        route = Screen.Deposit.route,
                    ) {
                        DepositScreen(
                            navHostController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = iconMap[screen.resourceId] ?: Icons.Default.Warning,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        maxLines = 1,
                        text = stringResource(screen.resourceId),
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}


val items = listOf(
    Screen.Overview,
    Screen.Analytics,
    Screen.Transactions,
    Screen.Goals,
)

val iconMap = mapOf(
    Screen.Analytics.resourceId to Icons.Filled.Analytics,
    Screen.Transactions.resourceId to Icons.Filled.SwapVert,
    Screen.Overview.resourceId to Icons.Filled.Home,
    Screen.Goals.resourceId to Icons.Filled.Person,
)




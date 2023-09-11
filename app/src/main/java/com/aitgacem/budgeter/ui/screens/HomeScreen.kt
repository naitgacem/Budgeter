package com.aitgacem.budgeter.ui.screens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aitgacem.budgeter.ui.components.Screen


@Composable
fun HomeScreen(
    topLevelNavHost: NavHostController,
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = { fadeIn() },
            ) {
                OverviewScreen(
                    navController = topLevelNavHost,
                )
            }

            composable(
                route = Screen.Transactions.route,
                enterTransition = { fadeIn() },
            ) { TransactionsScreen(navController = navController) }


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

private val items = listOf(
    Screen.Home,
    Screen.Analytics,
    Screen.Transactions,
    Screen.Goals,
)
private val iconMap = mapOf(
    Screen.Analytics.resourceId to Icons.Filled.Analytics,
    Screen.Transactions.resourceId to Icons.Filled.SwapVert,
    Screen.Home.resourceId to Icons.Filled.Home,
    Screen.Goals.resourceId to Icons.Filled.Person,
)
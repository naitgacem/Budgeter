package com.codelab.basiclayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codelab.basiclayouts.ui.components.Screen
import com.codelab.basiclayouts.ui.screens.AddTransactionScreen
import com.codelab.basiclayouts.ui.screens.HistoryScreen
import com.codelab.basiclayouts.ui.screens.OverviewScreen
import com.codelab.basiclayouts.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { NavBar(navController = navController) }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Overview.route,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    composable(route = Screen.Overview.route) {
                        OverviewScreen(navController = navController)
                    }
                    composable(route = Screen.Transactions.route) {
                        HistoryScreen()
                    }
                    composable(route = Screen.Settings.route){
                        SettingsScreen()
                    }
                    composable(route = Screen.AddTransaction.route){
                        AddTransactionScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
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
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }

    }
}


val items = listOf(
    Screen.Overview,
    Screen.Search,
    Screen.Transactions,
    Screen.Profile,
)





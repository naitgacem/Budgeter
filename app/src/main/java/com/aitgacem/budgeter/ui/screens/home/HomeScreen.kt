package com.aitgacem.budgeter.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.ui.screens.NavGraphs
import com.aitgacem.budgeter.ui.screens.appCurrentDestinationAsState
import com.aitgacem.budgeter.ui.screens.destinations.AnalyticsScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.GoalsScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.OverviewScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.TransactionsScreenDestination
import com.aitgacem.budgeter.ui.screens.startAppDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@RootNavGraph(start = true)
@NavGraph
annotation class HomeNavGraph(
    val start: Boolean = false,
)

@HomeNavGraph(start = true)
@Destination

@Composable
fun HomeScreen(
    topLevelnavController: DestinationsNavigator,
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBar(navController)
        }
    ) {
        DestinationsNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            navGraph = NavGraphs.home,
            startRoute = OverviewScreenDestination,
        ) {
            composable(OverviewScreenDestination) {
                OverviewScreen(navigator = topLevelnavController)
            }
            composable(TransactionsScreenDestination) {
                TransactionsScreen(navigator = topLevelnavController)
            }
        }
    }
}

@Composable
fun NavBar(
    navController: NavController,
) {
    NavigationBar {
        BottomBarDestination.values().forEach { destination ->

            val currentDestination = navController.appCurrentDestinationAsState().value
                ?: NavGraphs.home.startAppDestination
            val isSelected = currentDestination == destination.direction

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        navController.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }
                    navController.navigate(destination.direction) {
                        popUpTo(NavGraphs.home) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.resId),
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

@Immutable
enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val resId: Int,
    @StringRes val label: Int,
) {
    Overview(OverviewScreenDestination, R.drawable.ic_home, R.string.home),
    Analytics(AnalyticsScreenDestination, R.drawable.ic_analytics, R.string.analytics),
    Transaction(TransactionsScreenDestination, R.drawable.ic_swap_vert, R.string.transactions),
    Goals(GoalsScreenDestination, R.drawable.ic_person, R.string.goals),
}
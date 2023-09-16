package com.aitgacem.budgeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.screens.DepositScreen
import com.aitgacem.budgeter.ui.screens.NavGraphs
import com.aitgacem.budgeter.ui.screens.home.HomeScreen
import com.aitgacem.budgeter.ui.screens.SettingsScreen
import com.aitgacem.budgeter.ui.screens.TransactionDetailsScreen
import com.aitgacem.budgeter.ui.screens.WithdrawScreen
import com.aitgacem.budgeter.ui.screens.destinations.HomeScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.OverviewScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.SettingsScreenDestination
import com.aitgacem.budgeter.ui.screens.home.OverviewScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}




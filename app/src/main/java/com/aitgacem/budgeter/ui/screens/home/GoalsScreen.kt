package com.aitgacem.budgeter.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph
@Destination
@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Text(
        text = "Goals screen"
    )
}
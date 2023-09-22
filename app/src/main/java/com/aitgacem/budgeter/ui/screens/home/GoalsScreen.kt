package com.aitgacem.budgeter.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeNavGraph
@Destination
@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
) {
    Text(
        text = "Goals screen"
    )
}
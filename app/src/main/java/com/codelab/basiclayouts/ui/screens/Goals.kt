package com.codelab.basiclayouts.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Text(
        text = "Goals screen"
    )
}
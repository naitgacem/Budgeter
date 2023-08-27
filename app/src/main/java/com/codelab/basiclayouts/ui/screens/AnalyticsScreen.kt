package com.codelab.basiclayouts.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
@Composable
fun AnalyticsScreen(
    navController: NavHostController
) {
    Surface {
        Text(text = "analytics screen")
    }
}

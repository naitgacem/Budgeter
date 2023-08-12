package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codelab.basiclayouts.ui.components.ContentArea
import com.codelab.basiclayouts.ui.components.NavBar
import com.codelab.basiclayouts.ui.components.TopBar


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: () -> Unit) {

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { NavBar() }
    ) { paddingValues ->
        ContentArea(Modifier.padding(paddingValues))
    }
}
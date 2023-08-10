package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codelab.basiclayouts.ui.components.ContentArea
import com.codelab.basiclayouts.ui.components.NavBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Scaffold(
        bottomBar = { NavBar() }
    ) { paddingValues ->
        ContentArea(Modifier.padding(paddingValues))
    }
}
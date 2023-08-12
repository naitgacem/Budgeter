package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.components.ContentArea
import com.codelab.basiclayouts.ui.components.NavBar
@Preview
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Scaffold(
        topBar = { },
        bottomBar = { NavBar() }
    ) { paddingValues ->
        ContentArea(Modifier.padding(paddingValues))
    }
}
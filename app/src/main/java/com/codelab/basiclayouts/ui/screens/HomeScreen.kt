package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.codelab.basiclayouts.ui.components.ContentArea


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToAddTransaction: () -> Unit,
    navigateToHistory: () -> Unit,

) {

        Scaffold(
            topBar = { TopBar(navigateToSettings) },
            bottomBar = { NavBar(navigateToHistory = navigateToHistory) }
        ) { paddingValues ->
            ContentArea(
                Modifier.padding(paddingValues),
                addTransactionNavController = navigateToAddTransaction
            )
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                text = "budgeter",
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null
            )
        },
        actions = {
            IconButton(onClick = navController) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navigateToHistory: () -> Unit,
           ) {
    BottomAppBar {
        NavigationBarItem(modifier = modifier,
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) })
        NavigationBarItem(
            modifier = modifier,
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            },
        )
        NavigationBarItem(modifier = modifier,
            selected = false,
            onClick = navigateToHistory,
            icon = { Icon(imageVector = Icons.Filled.SwapVert, contentDescription = null) }
        )
        NavigationBarItem(modifier = modifier,
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = null) }
        )
    }
}
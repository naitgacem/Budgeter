package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codelab.basiclayouts.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()}
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { paddingValues ->
        SettingsList(modifier = Modifier.padding(paddingValues))
    }

}


@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(settingsMenuItems) { item ->
            SettingCategoryDisplay(
                icon = item.icon,
                title = item.title,
                description = item.description
            )
        }

    }
}

@Composable
fun SettingCategoryDisplay(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = modifier
                .size(width = 40.dp, height = 40.dp)
                .padding(all = 8.dp)
        )
        Column {
            Text(
                text = title,
                textAlign = TextAlign.Left,

                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .padding(horizontal = 8.dp)

            )
            Text(
                modifier = modifier
                    .padding(horizontal = 8.dp),
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}


data class SettingMenuItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
)

val settingsMenuItems = listOf(
    SettingMenuItem(
        icon = Icons.Filled.Tune,
        title = "General",
        description = "Currency selection, language preferences"
    ),
    SettingMenuItem(
        icon = Icons.Filled.Payments,
        title = "Income and expenses",
        description = "Income categories, sources, budget limits"
    ),
    SettingMenuItem(
        icon = Icons.Filled.CreditCard,
        title = "Transactions",
        description = "Recurring transactions, custom types"
    ),
    SettingMenuItem(
        icon = Icons.Filled.Notifications,
        title = "Notifications",
        description = "Budget alerts, bill reminders"
    ),
    SettingMenuItem(
        icon = Icons.Filled.Storage,
        title = "Data management",
        description = "Import, export, and share budget data"
    ),
    SettingMenuItem(
        icon = Icons.Filled.Help,
        title = "Support and help",
        description = "FAQs, contact, app version"
    ),
)

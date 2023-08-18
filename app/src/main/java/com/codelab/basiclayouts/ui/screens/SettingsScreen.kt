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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.R
import com.codelab.basiclayouts.ui.theme.typography

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                )
            }
        ) { paddingValues ->
            SettingsList(modifier = Modifier.padding(paddingValues))
        }
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
    Button(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .height(72.dp)
            .clip(RectangleShape),
        onClick = {},

        ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
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

                    style = typography.h2,
                    modifier = modifier
                        .padding(horizontal = 8.dp)

                )
                Text(
                    modifier = modifier
                        .padding(horizontal = 8.dp),
                    text = description,
                    style = typography.caption
                )
            }
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

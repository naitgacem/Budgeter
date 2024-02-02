package com.aitgacem.budgeter.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aitgacem.budgeter.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(

) {
    Surface {
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
                            onClick = { }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
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
                resId = item.resId,
                title = item.title,
                description = item.description
            )
        }

    }
}

@Composable
fun SettingCategoryDisplay(
    modifier: Modifier = Modifier,
    resId: Int,
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
            painter = painterResource(id = resId),
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
    val resId: Int,
    val title: String,
    val description: String,
)

val settingsMenuItems = listOf(
    SettingMenuItem(
        resId = R.drawable.ic_tune,
        title = "General",
        description = "Currency selection, language preferences"
    ),
    SettingMenuItem(
        resId = R.drawable.ic_payments,
        title = "Income and expenses",
        description = "Income categories, sources, budget limits"
    ),
    SettingMenuItem(
        resId = R.drawable.ic_credit_card,
        title = "Transactions",
        description = "Recurring transactions, custom types"
    ),
    SettingMenuItem(
        resId = R.drawable.ic_notifications,
        title = "Notifications",
        description = "Budget alerts, bill reminders"
    ),
    SettingMenuItem(
        resId = R.drawable.ic_storage,
        title = "Data management",
        description = "Import, export, and share budget data"
    ),
    SettingMenuItem(
        resId = R.drawable.ic_help,
        title = "Support and help",
        description = "FAQs, contact, app version"
    ),
)

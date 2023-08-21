package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.codelab.basiclayouts.R
import com.codelab.basiclayouts.data.model.Transaction
import com.codelab.basiclayouts.ui.components.Screen
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import com.codelab.basiclayouts.ui.viewmodels.DataViewModel

@Composable
fun OverviewScreen(
    modifier: Modifier = Modifier,
    overviewNavController: NavHostController,
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory),
) {
    val recentTransactionsState = dataViewModel.recentTransactions.collectAsState()
    val balance = dataViewModel.balance.collectAsState()
    LazyColumn(
        modifier = modifier
    ) {

        item {
            TopSection(
                balance = balance.value,
                navigateToWithdraw = {
                    overviewNavController.navigate(Screen.Withdraw.route)
                },
                navigateToDeposit = {
                    overviewNavController.navigate(Screen.Deposit.route)
                },
                navigateToSettings = { overviewNavController.navigate(Screen.Settings.route) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
        items(recentTransactionsState.value) { day ->
            ItemDisplay(
                transaction = day,
                navigateToItem = {
                    overviewNavController.navigate(
                        Screen.TransactionDetails.route.replace(
                            oldValue = "{id}",
                            newValue = day.id.toString()
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun ItemDisplay(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    navigateToItem: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = navigateToItem)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = categoryToIconMap[transaction.category] ?: Icons.Filled.Warning,
            contentDescription = "",
            modifier = modifier
                .size(width = 40.dp, height = 40.dp)
                .padding(all = 8.dp)
        )
        Text(
            text = transaction.title,
            textAlign = TextAlign.Left,
            modifier = modifier
                .padding(horizontal = 8.dp)
                .weight(.6f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = transaction.amount.toString() + " DA",
            textAlign = TextAlign.End,
            modifier = modifier.weight(.35f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    balance: Int,
    navigateToWithdraw: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToDeposit: () -> Unit,
) {
    Column {
        TopBar(navigateToSettings)
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row {
                Budget(
                    modifier
                        .weight(1f)
                        .height(132.dp),
                    balance = balance,

                )
                Operations(modifier.weight(1f), navigateToWithdraw, navigateToDeposit)
            }
        }
    }
}

@Composable
fun Operations(
    modifier: Modifier = Modifier,
    navigateToWithdraw: () -> Unit,
    navigateToDeposit: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(
            onClick = navigateToDeposit,
            modifier = modifier
                .padding(8.dp)
                .width(120.dp)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.deposit))
        }
        FilledIconButton(
            onClick = navigateToWithdraw,
            modifier = modifier
                .padding(8.dp)
                .width(120.dp)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.withdraw))
        }
    }
}

@Composable
fun Budget(
    modifier: Modifier = Modifier,
    balance: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = balance.toString(),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "DA",
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateToSettings: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.budgeter),
                textAlign = TextAlign.Center,
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Person, contentDescription = null
            )
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
        }
    )
}
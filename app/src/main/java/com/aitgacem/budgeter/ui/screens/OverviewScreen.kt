package com.aitgacem.budgeter.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.components.categoryToIconMap
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    overviewNavController: NavHostController,
    overviewViewModel: OverviewViewModel = viewModel(factory = OverviewViewModel.Factory),
) {
    val recentTransactions by overviewViewModel.recentTransactions.collectAsState()
    val balance by overviewViewModel.balance.collectAsState()


    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.budgeter),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Filled.Savings, contentDescription = null
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                overviewNavController.navigate(Screen.Settings.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            OverviewScreenContent(
                modifier = Modifier.padding(paddingValues),
                balance = balance,
                recentTransactions = recentTransactions,
                navController = overviewNavController
            )
        }
    }
}

@Composable
private fun OverviewScreenContent(
    modifier: Modifier = Modifier,
    balance: Int,
    recentTransactions: List<Transaction>,
    navController: NavController,
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Budget(
                        modifier = Modifier.weight(.45f),
                        balance = balance,
                    )
                    Operations(
                        modifier = Modifier.weight(.45f),
                        navigateToWithdraw = { navController.navigate(Screen.Withdraw.route) },
                        navigateToDeposit = { navController.navigate(Screen.Deposit.route) }
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
        item {
            Text(
                modifier = Modifier.padding(bottom = 20.dp, start = 30.dp),
                text = "Recent transactions",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(
            key = { it.id },
            items = recentTransactions,
        ) { day ->
            ItemDisplay(
                transaction = day,
                navigateToItem = {
                    navController.navigate(
                        Screen.TransactionDetails.route
                            .replace(oldValue = "{id}", newValue = day.id.toString())
                    )
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 30.dp),
                thickness = Dp.Hairline
            )
        }
    }
}

@Composable
private fun ItemDisplay(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    navigateToItem: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                onClick = navigateToItem,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = if (transaction.amount == 0) {
                Icons.Default.Edit
            } else if (transaction.amount > 0) {
                Icons.Default.ArrowUpward
            } else {
                categoryToIconMap[transaction.category] ?: Icons.Filled.Warning
            },
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
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
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
private fun Operations(
    modifier: Modifier = Modifier,
    navigateToWithdraw: () -> Unit,
    navigateToDeposit: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FilledIconButton(
            onClick = navigateToDeposit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(IntrinsicSize.Max)
        ) {
            Text(
                text = stringResource(R.string.deposit)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilledIconButton(
            onClick = navigateToWithdraw,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(IntrinsicSize.Max)
        ) {
            Text(
                text = stringResource(R.string.withdraw)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OperationsPreview() {
    Budget(balance = 500)
}

@Composable
private fun Budget(
    modifier: Modifier = Modifier,
    balance: Int,
) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "$balance DA",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

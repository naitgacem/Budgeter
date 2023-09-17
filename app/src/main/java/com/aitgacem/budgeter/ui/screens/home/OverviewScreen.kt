package com.aitgacem.budgeter.ui.screens.home

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
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.toIcon
import com.aitgacem.budgeter.ui.screens.destinations.DepositScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.SettingsScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.TransactionDetailsScreenDestination
import com.aitgacem.budgeter.ui.screens.destinations.WithdrawScreenDestination
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.DecimalFormat


@HomeNavGraph
@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navigator: DestinationsNavigator,
    overviewViewModel: OverviewViewModel = hiltViewModel(),
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
                                navigator.navigate(SettingsScreenDestination)
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
                navigator = navigator
            )
        }
    }
}

@Composable
private fun OverviewScreenContent(
    modifier: Modifier = Modifier,
    balance: Float,
    recentTransactions: List<Transaction>,
    navigator: DestinationsNavigator,
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
                        navigateToWithdraw = { navigator.navigate(WithdrawScreenDestination) },
                        navigateToDeposit = { navigator.navigate(DepositScreenDestination(null)) }
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
                    navigator.navigate(
                        TransactionDetailsScreenDestination(transaction = day)
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
            imageVector = transaction.category.toIcon(),
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
    Budget(balance = 500f)
}

@Composable
private fun Budget(
    modifier: Modifier = Modifier,
    balance: Float,
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
                text = "${DecimalFormat("#.##").format(balance)} DA",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

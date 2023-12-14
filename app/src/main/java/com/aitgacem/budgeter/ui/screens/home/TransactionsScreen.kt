package com.aitgacem.budgeter.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.components.toIcon
import com.aitgacem.budgeter.ui.screens.destinations.TransactionDetailsScreenDestination
import com.aitgacem.budgeter.ui.viewmodels.TransactionsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.util.Date

@HomeNavGraph
@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
) {
    val listOfAllTransactions by transactionsViewModel.allTransactions.collectAsState()


    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearchBarActive by remember { mutableStateOf(false) }

    val listOfDays = arrangeIntoDays(filterTransaction(listOfAllTransactions, searchText))
    Surface {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                DockedSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            // end = 16.dp,
                            top = 16.dp,
                            bottom = 20.dp
                        ),
                    placeholder = { Text("Search for transactions") },
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { isSearchBarActive = false },
                    active = isSearchBarActive,
                    onActiveChange = { isSearchBarActive = it },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                ) {

                }
            }
            items(
                items = listOfDays,
            ) { day ->
                Text(
                    modifier = modifier.padding(start = 24.dp),
                    text = day.date,
                    style = MaterialTheme.typography.bodyLarge
                )
                Card(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    for (transaction in day.transactions) {
                        ItemContent(
                            transaction = transaction,
                            navigateToItem = {
                                navigator.navigate(TransactionDetailsScreenDestination(transaction = transaction))
                            }
                        )
                        Divider(
                            thickness = Dp.Hairline,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            startIndent = 30.dp
                        )
                    }
                }
                Spacer(modifier = modifier.height(12.dp))
            }

        }
    }
}

@Composable
private fun ItemContent(
    transaction: Transaction,
    navigateToItem: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = navigateToItem),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = transaction.category.toIcon(),
            contentDescription = "",
            modifier = Modifier
                .weight(.1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .weight(.6f),

            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = transaction.title,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Left,
                text = transaction.amount.toString() + " DA",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
        }
        Text(
            modifier = Modifier.weight(.3f),
            text = "09:42",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.End,
        )
    }
}

private fun arrangeIntoDays(transactions: List<Transaction>): List<Day> {
    val listOfDays = mutableListOf<Day>()
    val groupOfDays = transactions.groupBy { it.date }
    for (day in groupOfDays.entries) {
        listOfDays += Day(
            date = SimpleDateFormat("dd/MM/yyyy").format(Date(day.key)),
            transactions = day.value
        )
    }
    return listOfDays
}

private fun filterTransaction(transactions: List<Transaction>, filter: String): List<Transaction> {
    if (filter == "") {
        return transactions
    }
    val result = mutableListOf<Transaction>()
    for (transaction in transactions) {
        if (transaction.title.contains(filter, ignoreCase = true)) {
            result.add(transaction)
        }
    }
    return result

}

private data class Day(
    val date: String,
    val transactions: List<Transaction>,
)

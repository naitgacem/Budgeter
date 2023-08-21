package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codelab.basiclayouts.data.model.Transaction
import com.codelab.basiclayouts.ui.components.Screen
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import com.codelab.basiclayouts.ui.viewmodels.DataViewModel
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory),
) {
    val listOfAllTransactions by dataViewModel.allTransactions.collectAsState()
    val listOfDays = arrangeIntoDays(listOfAllTransactions)

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            DockedSearchBar(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 20.dp
                ),
                query = "Search a transaction",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            ) {
            }
        }
        items(listOfDays) { day ->
            Text(
                modifier = modifier.padding(start = 28.dp),
                text = day.date,
                style = MaterialTheme.typography.bodyLarge
            )
            Card {
                for (transaction in day.transactions) {
                    ItemContent(
                        transaction = transaction,
                        navigateToItem = {
                            navController.navigate(
                                Screen.TransactionDetails.route
                                    .replace(oldValue = "{id}", newValue = transaction.id.toString())
                            )
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

@Composable
fun ItemContent(
    transaction: Transaction,
    navigateToItem: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable  (onClick = navigateToItem) ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = categoryToIconMap[transaction.category] ?: Icons.Default.Warning,
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
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Left,
                text = transaction.amount.toString() + " DA",
                style = MaterialTheme.typography.bodyMedium
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

fun arrangeIntoDays(transactions: List<Transaction>): List<Day> {
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

data class Day(
    val date: String,
    val transactions: List<Transaction>,
)

val listOfTransactions = listOf(
    Transaction(
        id = 110,
        date = 100,
        title = "Train",
        amount = 50,
        category = "Transportation"
    ),
    Transaction(
        id = 120,
        date = 100,
        title = "Coffee",
        amount = 30,
        category = "Food"
    ),
    Transaction(
        id = 140,
        date = 100,
        title = "Photo copy",
        amount = 20,
        category = "Education"
    ),
    Transaction(
        id = 150,
        date = 100,
        title = "Pizza",
        amount = 100,
        category = "Food"
    )
)
package com.aitgacem.budgeter.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.Screen
import com.aitgacem.budgeter.ui.components.categoryToIconMap
import com.aitgacem.budgeter.ui.viewmodels.TransactionDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    id: String,
    transactionDetailsViewModel: TransactionDetailsViewModel = hiltViewModel(),
    navController: NavController,
) {
    transactionDetailsViewModel.loadTransaction(id = id.toLong())
    val transaction by transactionDetailsViewModel.transaction.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Filled.AttachFile, contentDescription = null)
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(
                                Screen.Deposit.route.replace(
                                    oldValue = "{id}", newValue = id
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        TransactionDetailsContent(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(paddingValues),
            transaction = transaction ?: Transaction(0, 0, "", 0.toFloat(), Category.Others)
        )
    }

}

@Composable
fun TransactionDetailsContent(
    modifier: Modifier = Modifier,
    transaction: Transaction,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp),
            text = "Transaction Details",
            style = MaterialTheme.typography.headlineMedium
        )
        Card(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            border = BorderStroke(width = Dp.Hairline, color = MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            )
        ) {
            HeaderContent(transaction = transaction)
        }
        Text(
            modifier = Modifier.padding(start = 32.dp, top = 32.dp),
            text = "Description",
            style = MaterialTheme.typography.titleLarge
        )
        Box(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .border(
                    width = Dp.Hairline,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp),
                text = transaction.title,
                textAlign = TextAlign.Left,
                minLines = 8,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun HeaderContent(transaction: Transaction) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (transaction.category == Category.Deposit) {
                Icons.Default.ArrowUpward
            } else {
                categoryToIconMap[transaction.category] ?: Icons.Filled.Warning
            },
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
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Left,
                text = transaction.amount.toString() + " DA",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            modifier = Modifier.weight(.3f),
            text = SimpleDateFormat("dd/MM/yyyy").format(Date(transaction.date)),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
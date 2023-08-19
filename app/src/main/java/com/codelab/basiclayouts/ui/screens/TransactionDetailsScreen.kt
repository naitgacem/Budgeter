package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelab.basiclayouts.data.model.Transaction
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import com.codelab.basiclayouts.ui.viewmodels.DataViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    id: String,
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory),
) {
    val transaction = dataViewModel.loadTransaction(id = id)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
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
                        onClick = {}
                    ) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                }

            )
        }
    ) { paddingValues ->
        Header(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(paddingValues),
            transaction = transaction
        )

    }

}

@Composable
fun Header(
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
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        style = MaterialTheme.typography.titleMedium
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
                    text = SimpleDateFormat("dd/MM/yyyy").format(Date(transaction.dateAndTime)),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
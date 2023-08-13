package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelab.basiclayouts.ui.viewmodels.DataViewModel

@Composable
fun ContentArea(
    modifier: Modifier = Modifier,
    addTransactionNavController: () -> Unit,
    dataViewModel: DataViewModel = viewModel(factory = DataViewModel.Factory)
) {

    val recentTransactionsState = dataViewModel.recentTransactions.collectAsState()

    LazyColumn() {
        item {
            TopSection(addTransactionNavController = addTransactionNavController)
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

        items(recentTransactionsState.value) { day ->
            ItemDisplay(transaction = day)
        }
    }
}



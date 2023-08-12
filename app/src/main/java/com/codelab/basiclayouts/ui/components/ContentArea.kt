package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Details
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelab.basiclayouts.ui.viewmodels.DayViewModel

@Composable
fun ContentArea(
                modifier: Modifier = Modifier,
                viewModel: DayViewModel = viewModel()
){
    val daySummary = viewModel.daySummary.observeAsState()
    val day = remember { viewModel.daySummary.value }
    val data: List<DaySummary> = listOf(day ?:  DaySummary(1970, 1, 1, listOf(Transaction(Icons.Filled.Details, "error", 0))))
    LazyColumn() {
        item {
            TopSection()
        }
        item{
            Spacer(modifier = Modifier.height(40.dp))
        }

        items(data){
                day -> DayDisplay(modifier = Modifier, day)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDisplayPreview(){
    ItemDisplay(transaction = Transaction(Icons.Filled.AcUnit, "Air Conditioner", 101))
}


@Preview
@Composable
fun ContentAreaPreview(){
    ContentArea()
}
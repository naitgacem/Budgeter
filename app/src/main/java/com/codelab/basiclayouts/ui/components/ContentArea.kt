package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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



    Column {
        TopSection()
        val day = remember { viewModel.daySummary.value }
        val data: List<DaySummary> = listOf(day ?:  DaySummary(1970, 1, 1, listOf(Transaction(Icons.Filled.Details, "error", 0))))
        LazyColumn(
            modifier = modifier
        ){
            items(data){
                    day -> DayDisplay(day)
            }
        }
    }

}

@Composable
fun DayDisplay(
    day: DaySummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(120.dp)
    ){

        Text(text = "${day.day} - ${day.month} - ${day.year}")
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            contentPadding = PaddingValues(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)

        ){
            items(day.spending){
                item -> ItemDisplay(transaction = item)
            }
        }
    }
}

@Composable
fun ItemDisplay(modifier: Modifier = Modifier, transaction: Transaction){
    Row(

        modifier = modifier.border(1.dp, Color.Black)
    ) {
        Icon(
            imageVector = transaction.icon ,
            contentDescription = "",
            modifier = modifier.weight(0.1f)
        )
        Text(
            text = transaction.title,
            modifier = modifier
                .weight(0.6f)
                .padding(horizontal = 12.dp)
        )
        Text(
            text = transaction.amount.toString(),
            textAlign = TextAlign.Center,
            modifier = modifier.weight(0.3f)
        )

    }
}

@Preview
@Composable
fun ItemDisplayPreview(){
    ItemDisplay(transaction = Transaction(Icons.Filled.AcUnit, "Air Conditioner", 101))
}

@Preview
@Composable
fun DayDisplayPreview(){
    //TODO("This should be a function that reads from JSON or something then calls the appropriate function")
    val transaction1 = Transaction(Icons.Filled.AcUnit, "Air Conditioner", 101)
    val transaction2 = Transaction(Icons.Filled.Apps, "Premium App", 102)
    val transaction3 = Transaction(Icons.Filled.LocalPhone, "Phone bill", 103)

    val spending: List<Transaction> = listOf(transaction1, transaction2, transaction3)
    val day = DaySummary(2023, 8, 10, spending)

    DayDisplay(day)
}

@Preview
@Composable
fun ContentAreaPreview(){
    ContentArea()
}
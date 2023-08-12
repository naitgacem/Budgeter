package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Details
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

@Composable
fun DayDisplay(
    modifier: Modifier = Modifier,
    day: DaySummary

) {
   Column(
        modifier = modifier
    ){


       Card {
           DisplayDate(day = day.day, month = day.month, year = day.year)
           Divider(thickness = 1.dp)
           for(item in day.spending){
               ItemDisplay(transaction = item )
               Divider(thickness = Dp.Hairline)
           }
       }

    }

}

@Composable
fun ItemDisplay(modifier: Modifier = Modifier, transaction: Transaction){

       Row(
           modifier = modifier
               .padding(vertical = 2.dp)
               .padding(all = 8.dp)
               .fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically

       ) {
           Icon(
               imageVector = transaction.icon ,
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
                   .weight(.7f)


           )
           Text(
               text = transaction.amount.toString(),
               textAlign = TextAlign.Center,
               modifier = modifier.width(40.dp)
           )

       }
}

@Composable
fun DisplayDate(day: Int, month: Int, year: Int){
    Text(
        text = "$day - $month - $year",
        style = MaterialTheme.typography.labelLarge
    )
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
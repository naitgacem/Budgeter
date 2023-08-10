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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContentArea(modifier: Modifier = Modifier){
    Column {
        TopSection()
        val item1 = Item(Icons.Filled.AcUnit, "Air Conditioner", 101)
        val item2 = Item(Icons.Filled.Apps, "Premium App", 102)
        val item3 = Item(Icons.Filled.LocalPhone, "Phone bill", 103)

        val spending: List<Item> = listOf(item1, item2, item3)
        val day = DaySummary(2023, 8, 10, spending)

        val item12 = Item(Icons.Filled.AdsClick, "Archery", 101)
        val item22 = Item(Icons.Filled.AccountBalance, "Lawyer", 102)


        val spending2: List<Item> = listOf(item12, item22)
        val day2 = DaySummary(2023, 8, 11, spending2)

        val data: List<DaySummary> = listOf(day, day2)
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
                item -> ItemDisplay(item = item)
            }
        }
    }
}

@Composable
fun ItemDisplay(modifier: Modifier = Modifier, item: Item){
    Row(

        modifier = modifier.border(1.dp, Color.Black)
    ) {
        Icon(
            imageVector = item.icon ,
            contentDescription = "",
            modifier = modifier.weight(0.1f)
        )
        Text(
            text = item.title,
            modifier = modifier
                .weight(0.6f)
                .padding(horizontal = 12.dp)
        )
        Text(
            text = item.amount.toString(),
            textAlign = TextAlign.Center,
            modifier = modifier.weight(0.3f)
        )

    }
}

@Preview
@Composable
fun ItemDisplayPreview(){
    ItemDisplay(item = Item(Icons.Filled.AcUnit, "Air Conditioner", 101))
}

@Preview
@Composable
fun DayDisplayPreview(){
    //TODO("This should be a function that reads from JSON or something then calls the appropriate function")
    val item1 = Item(Icons.Filled.AcUnit, "Air Conditioner", 101)
    val item2 = Item(Icons.Filled.Apps, "Premium App", 102)
    val item3 = Item(Icons.Filled.LocalPhone, "Phone bill", 103)

    val spending: List<Item> = listOf(item1, item2, item3)
    val day = DaySummary(2023, 8, 10, spending)

    DayDisplay(day)
}

@Preview
@Composable
fun ContentAreaPreview(){
    ContentArea()
}
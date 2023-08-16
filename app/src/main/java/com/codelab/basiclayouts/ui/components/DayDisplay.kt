package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.data.model.DaySummary
import com.codelab.basiclayouts.ui.screens.ItemDisplay

@Composable
fun DayDisplay(
    modifier: Modifier = Modifier,
    day: DaySummary

) {
    Column(
        modifier = modifier
    ) {


        Card {
            DisplayDate(day = day.day, month = day.month, year = day.year)
            HorizontalDivider(thickness = 1.dp)
            for (item in day.spending) {
                ItemDisplay(transaction = item)
                HorizontalDivider(thickness = Dp.Hairline)
            }
        }

    }

}

@Composable
fun DisplayDate(day: Int, month: Int, year: Int){
    Text(
        text = "$day - $month - $year",
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(8.dp)
    )
}
package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
            Divider(thickness = 1.dp)
            for (item in day.spending) {
                ItemDisplay(transaction = item)
                Divider(thickness = Dp.Hairline)
            }
        }

    }

}
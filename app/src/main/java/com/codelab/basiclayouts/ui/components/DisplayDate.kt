package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisplayDate(day: Int, month: Int, year: Int){
    Text(
        text = "$day - $month - $year",
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(8.dp)
    )
}
package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
            imageVector = transaction.icon,
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
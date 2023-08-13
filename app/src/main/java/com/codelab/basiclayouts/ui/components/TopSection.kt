package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    addTransactionNavController: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row {
            Budget(
                modifier
                    .weight(1f)
                    .height(132.dp)

            )
            Operations(modifier.weight(1f), addTransactionNavController)
        }
    }
}

@Composable
fun Operations(
    modifier: Modifier = Modifier,
    addTransactionNavController: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(
            onClick = {},

            modifier = modifier
                .padding(8.dp)
                .width(120.dp)
                .height(50.dp)

        ) {
            Text("Deposit")
        }
        FilledIconButton(
            onClick = addTransactionNavController,
            modifier = modifier
                .padding(8.dp)
                .width(120.dp)
                .height(50.dp)
        ) {
            Text("Withdraw")
        }
    }
}

@Composable
fun Budget(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = "500",
            style = MaterialTheme.typography.headlineMedium,

            )
        Text(
            text = "DZD",
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.labelMedium,


            )

    }

}


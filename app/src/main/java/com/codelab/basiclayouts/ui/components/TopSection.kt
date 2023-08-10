package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopSection(modifier: Modifier = Modifier){
    Row(
       modifier = modifier
           .fillMaxWidth()
           .height(IntrinsicSize.Min)
    ){
        Budget(
            modifier
                .weight(1f)
                .fillMaxHeight())
        Operations(modifier.weight(1f))
    }
}

@Composable
fun Operations(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {},
            modifier = modifier.padding(8.dp)
        ) {
            Text("Deposit")
        }
        Button(
            onClick = {},
            modifier = modifier.padding(8.dp)
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
        modifier = modifier.fillMaxWidth()
    ) {
        Text("500")
        Text("DZD")
    }
}

@Preview
@Composable
fun BudgetPreview(){
    Budget()
}

@Preview
@Composable
fun OperationsPreview(){
    Operations()
}

@Preview
@Composable
fun TopSectionPreview(){
    TopSection()
}



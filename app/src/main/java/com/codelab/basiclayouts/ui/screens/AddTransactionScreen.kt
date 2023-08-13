package com.codelab.basiclayouts.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.codelab.basiclayouts.R
import com.codelab.basiclayouts.ui.theme.typography

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.weight(.1f),
                        onClick = { /*TODO*/ },

                        ) {
                        Icon(
                            modifier = Modifier.weight(.1f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                    Spacer(modifier = Modifier.weight(.8f))
                    IconButton(
                        modifier = Modifier.weight(.1f),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            modifier = Modifier.weight(.1f),
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AddTransactionContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun AddTransactionContent(
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Add a transaction",
            style = typography.h1
        )
        TextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = "Amount")
            }
        )

        TextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = "Description")
            }
        )
    }
}
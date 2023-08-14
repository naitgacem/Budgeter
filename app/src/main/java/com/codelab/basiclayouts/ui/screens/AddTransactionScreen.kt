package com.codelab.basiclayouts.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.typography
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        AddTransactionContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    modifier: Modifier = Modifier
) {

    val state = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()


    ) {

        item {
            TitleBar()
        }
        item {
            InsertText(
                icon = Icons.Default.AttachMoney,
                label = "Amount",
                placeholder = "0"
            )
        }
        item {
            InsertText(
                icon = Icons.Default.Description,
                label = "Description",
                placeholder = ""
            )
        }

        item {
            DateSelect(state)
            Spacer(modifier = Modifier.height(16.dp))
        }


        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = modifier.weight(.1f),
                    imageVector = Icons.Default.Category, contentDescription = ""
                )
                OutlinedTextField(
                    modifier = modifier
                        .weight(.9f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),

                    label = { Text("Category") },

                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(text = "Others")
                    }
                )
            }
        }
        item {
            DropdownMenu(
                modifier = modifier.widthIn(170.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text("option 1")
                }
                DropdownMenuItem(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text("option 1")
                }
            }
        }



        item {
            Spacer(modifier = Modifier.height(24.dp))
            SaveButton()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelect(state: DatePickerState) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.weight(.1f),
            imageVector = Icons.Default.DateRange, contentDescription = ""
        )
        DatePicker(
            dateFormatter = DatePickerFormatter("dMMMy"),
            title = null,
            headline = null,
            modifier = Modifier.weight(.9f),
            showModeToggle = false,
            state = state,
        )

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {

        },
        actions = {
            IconButton(
                modifier = modifier.weight(.1f),
                onClick = { /*TODO*/ },

                ) {
                Icon(

                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.weight(.8f))
            IconButton(
                modifier = modifier.weight(.1f),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = ""
                )
            }
        }
    )
}



@Composable
fun InsertText(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    placeholder: String,

) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = modifier.weight(.1f),
            imageVector = icon, contentDescription = ""
        )
        OutlinedTextField(
            modifier = modifier
                .weight(.9f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            label = { Text(label) },

            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = placeholder)
            }
        )
    }

}

@Composable
fun SaveButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 32.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(

            onClick = { /*TODO*/ }) {
            Text(

                text = "Save"

            )
        }
    }
}
@Composable
private fun TitleBar(modifier: Modifier = Modifier){
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        text = "Add a transaction",
        style = typography.h1
    )
    Spacer(modifier = Modifier.height(16.dp))
}
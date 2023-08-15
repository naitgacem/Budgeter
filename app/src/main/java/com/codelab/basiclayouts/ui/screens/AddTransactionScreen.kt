package com.codelab.basiclayouts.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import com.codelab.basiclayouts.ui.theme.typography
import com.codelab.basiclayouts.ui.viewmodels.AddTransactionViewModel
import java.util.Calendar

@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,

    ) {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        AddTransactionContent(
            modifier = Modifier.padding(paddingValues),

            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    modifier: Modifier = Modifier,
    addTransactionViewModel: AddTransactionViewModel = viewModel(factory = AddTransactionViewModel.Factory)
) {
    val amount by addTransactionViewModel.amount.collectAsState()
    val description by addTransactionViewModel.description.collectAsState()
    val category by addTransactionViewModel.category.collectAsState()

    val state = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            TitleBar()
        }
        item {
            InsertAmount(
                value = amount,
                updateAmount = { addTransactionViewModel.updateAmount(it) }
            )
        }
        item {
            InsertDescription(
                value = description,
                updateDescription = {
                    addTransactionViewModel.updateDescription(it)
                }
            )
        }

        item {
            DateSelect(state)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            InsertCategory(
                menuItems = addTransactionViewModel.menuItems,
                selectedCategory = category,
                updateCategory = { addTransactionViewModel.updateCategory(it) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SaveButton(
                saveEntry = { addTransactionViewModel.saveTransaction()}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelect(state: DatePickerState) {
        DatePicker(
            modifier = Modifier,
            showModeToggle = true,
            state = state,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
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
fun InsertAmount(
    modifier: Modifier = Modifier,
    value: Int?,
    updateAmount: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = modifier.weight(.1f),
            imageVector = Icons.Default.AttachMoney, contentDescription = ""
        )
        OutlinedTextField(
            modifier = modifier
                .weight(.9f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Amount") },
            value = value?.toString() ?: "",
            onValueChange = updateAmount,
            placeholder = {
                Text(text = "0")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
fun InsertDescription(
    modifier: Modifier = Modifier,
    value: String?,
    updateDescription: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = modifier.weight(.1f),
            imageVector = Icons.Default.Description, contentDescription = ""
        )
        OutlinedTextField(
            modifier = modifier
                .weight(.9f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Description") },
            value = value ?: "",
            onValueChange = updateDescription,
        )
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    saveEntry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 32.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = saveEntry
        ) {
            Text(
                text = "Save"
            )
        }
    }
}

@Composable
private fun TitleBar(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        text = "Add a transaction",
        style = typography.h1
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun InsertCategory(
    modifier: Modifier = Modifier,
    menuItems: List<String>,
    selectedCategory: String,
    updateCategory: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {

        FlowRow(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.Start,
        ) {
            for (element in menuItems) {
                InputChip(
                    modifier = Modifier
                        .padding(2.dp)
                        .align(alignment = Alignment.CenterVertically),
                    label = { Text(element) },
                    onClick = { updateCategory(element) },
                    leadingIcon = {
                        Icon(
                            imageVector = categoryToIconMap[element] ?: Icons.Default.ErrorOutline,
                            contentDescription = "",
                        )
                    },
                    selected = element == selectedCategory
                )
            }
        }
    }
}

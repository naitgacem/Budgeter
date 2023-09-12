package com.aitgacem.budgeter.ui.screens


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
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.categoryToIconMap
import com.aitgacem.budgeter.ui.viewmodels.WithdrawViewModel
import com.ramcosta.composedestinations.annotation.Destination
import java.util.Calendar

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawScreen(
    navHostController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(
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
    ) { paddingValues ->
        AddTransactionContent(
            modifier = Modifier.padding(paddingValues)
        ) { navHostController.popBackStack() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionContent(
    modifier: Modifier = Modifier,
    withdrawViewModel: WithdrawViewModel = hiltViewModel(),
    exitAfterSave: () -> Boolean,
) {
    val amount by withdrawViewModel.amount.collectAsState()
    val description by withdrawViewModel.description.collectAsState()
    val category by withdrawViewModel.category.collectAsState()

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
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = "Add a transaction",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            InsertAmount(
                value = amount ?: "",
                updateAmount = { withdrawViewModel.updateAmount(it) }
            )
        }
        item {
            InsertDescription(
                value = description,
                updateDescription = {
                    withdrawViewModel.updateDescription(it)
                }
            )
        }
        item {
            DatePicker(
                modifier = Modifier,
                showModeToggle = true,
                state = state,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            InsertCategory(
                menuItems = withdrawViewModel.categories,
                selectedCategory = category,
                updateCategory = { withdrawViewModel.updateCategory(it) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SaveButton(
                saveEntry = {
                    withdrawViewModel.updateId(state.selectedDateMillis)
                    withdrawViewModel.saveTransaction()
                    exitAfterSave.invoke()
                }
            )
        }
    }
}


@Composable
private fun InsertAmount(
    modifier: Modifier = Modifier,
    value: String,
    updateAmount: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AttachMoney, contentDescription = ""
            )
        },
        label = { Text("Amount") },
        value = value,
        onValueChange = updateAmount,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun InsertDescription(
    value: String?,
    updateDescription: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Description, contentDescription = ""
            )
        },
        label = { Text("Description") },
        value = value ?: "",
        onValueChange = updateDescription,
    )

}

@Composable
private fun SaveButton(
    saveEntry: () -> Unit,
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

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun InsertCategory(
    modifier: Modifier = Modifier,
    menuItems: List<Category>,
    selectedCategory: Category?,
    updateCategory: (Category) -> Unit,
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
                    label = { Text(element.name) },
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

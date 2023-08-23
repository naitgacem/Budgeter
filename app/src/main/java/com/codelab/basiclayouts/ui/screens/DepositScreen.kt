package com.codelab.basiclayouts.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.codelab.basiclayouts.ui.viewmodels.DepositViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen(
    navHostController: NavHostController,
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
                        onClick = { }
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
        DepositContent(
            modifier = Modifier.padding(paddingValues)
        ) { navHostController.popBackStack() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DepositContent(
    modifier: Modifier = Modifier,
    depositViewModel: DepositViewModel = viewModel(factory = DepositViewModel.Factory),
    exitAfterSave: () -> Boolean,
) {
    val amount by depositViewModel.amount.collectAsState()
    val description by depositViewModel.description.collectAsState()

    val state = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    LazyColumn(
        //verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Add a deposit",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            InsertAmount(
                value = amount,
                updateAmount = { depositViewModel.updateAmount(it) }
            )
        }
        item {
            InsertDescription(
                value = description,
                updateDescription = {
                    depositViewModel.updateDescription(it)
                }
            )
        }

        item {
            DateSelect(state)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            SaveButton(
                saveEntry = {
                    depositViewModel.updateId(state.selectedDateMillis)
                    depositViewModel.saveTransaction()
                    exitAfterSave.invoke()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelect(state: DatePickerState) {
    DatePicker(
        modifier = Modifier,
        showModeToggle = true,
        state = state,
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



@Composable
private fun InsertDescription(
    modifier: Modifier = Modifier,
    value: String?,
    updateDescription: (String) -> Unit,
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = ""
            )
        },
        label = { Text("Description") },
        value = value ?: "",
        onValueChange = updateDescription,
    )

}


@Composable
private fun InsertAmount(
    modifier: Modifier = Modifier,
    value: Int?,
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
        value = value?.toString() ?: "",
        onValueChange = updateAmount,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}
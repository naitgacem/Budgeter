package com.codelab.basiclayouts.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import com.codelab.basiclayouts.ui.theme.typography
import com.codelab.basiclayouts.ui.viewmodels.DepositViewModel
import java.util.Calendar

@Composable
fun DepositScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,

    ) {
    Scaffold(
        topBar = {
            TopBar(navigateToOverview = {
                navHostController.popBackStack()
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
fun DepositContent(
    modifier: Modifier = Modifier,
    depositViewModel: DepositViewModel = viewModel(factory = DepositViewModel.Factory),
    exitAfterSave: () -> Boolean,
) {
    val amount by depositViewModel.amount.collectAsState()
    val description by depositViewModel.description.collectAsState()
    val category by depositViewModel.category.collectAsState()

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
            InsertCategory(
                menuItems = depositViewModel.menuItems,
                selectedCategory = category,
                updateCategory = { depositViewModel.updateCategory(it) }
            )
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
private fun TopBar(
    modifier: Modifier = Modifier,
    navigateToOverview: () -> Unit,
) {
    TopAppBar(
        title = {
        },
        navigationIcon = {
            IconButton(
                onClick = navigateToOverview,
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

@Composable
private fun TitleBar(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        text = "Add a deposit",
        style = typography.h1
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun InsertCategory(
    modifier: Modifier = Modifier,
    menuItems: List<String>,
    selectedCategory: String,
    updateCategory: (String) -> Unit,
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

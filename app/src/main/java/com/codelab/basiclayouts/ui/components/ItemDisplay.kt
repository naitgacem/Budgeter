package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
            imageVector = categoryToIconMap[transaction.category] ?: Icons.Filled.Warning ,
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

data class SpendingCategory (
        val category: String,
        val icon: ImageVector,
        )

private val categoryToIconMap = mapOf(
    "Housing" to Icons.Default.Apartment,
    "Transportation" to Icons.Default.DirectionsBus,
    "Groceries" to Icons.Default.ShoppingCart,
    "Entertainment" to Icons.Default.Movie,
    "Healthcare" to Icons.Default.Spa,
    "Education" to Icons.Default.School,
    "Travel" to Icons.Default.Map,
    "Utilities" to Icons.Default.Phone,
    "Others" to Icons.Default.Edit
)


private val spandingCategorisses = listOf<SpendingCategory>(
    SpendingCategory(
        category = "Housing",
        icon = Icons.Filled.Apartment
    ),
    SpendingCategory(
        category = "Transportation",
        icon = Icons.Filled.DirectionsBus
    ),
    SpendingCategory(
        category = "Groceries",
        icon = Icons.Filled.ShoppingCart
    ),
    SpendingCategory(
        category = "Entertainment",
        icon = Icons.Filled.Movie
    ),
    SpendingCategory(
        category = "Healthcare",
        icon = Icons.Filled.Spa
    ),
    SpendingCategory(
        category = "Education",
        icon = Icons.Filled.School
    ),
    SpendingCategory(
        category = "Travel",
        icon = Icons.Filled.Map
    ),
    SpendingCategory(
        category = "Utilities",
        icon = Icons.Filled.Phone
    ),
    SpendingCategory(
        category = "Others",
        icon = Icons.Filled.Edit
    )
)
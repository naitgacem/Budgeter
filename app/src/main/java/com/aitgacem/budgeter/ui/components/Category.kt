package com.aitgacem.budgeter.ui.components

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Category : Parcelable {
    Transportation,
    Groceries,
    Food,
    Travel,
    Utilities,
    Entertainment,
    Housing,
    Education,
    Healthcare,
    Others,
    Deposit
}

fun Category.toIcon(): ImageVector {
    return when (this) {
        Category.Transportation -> Icons.Default.DirectionsBus
        Category.Groceries -> Icons.Default.ShoppingCart
        Category.Travel -> Icons.Default.Map
        Category.Utilities -> Icons.Default.Phone
        Category.Entertainment -> Icons.Default.Movie
        Category.Housing -> Icons.Default.Apartment
        Category.Education -> Icons.Default.School
        Category.Healthcare -> Icons.Default.Spa
        Category.Food -> Icons.Default.Restaurant
        Category.Others -> Icons.Default.Edit
        Category.Deposit -> Icons.Default.ArrowUpward
    }
}
package com.codelab.basiclayouts.ui.components

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
enum class Category {
   Transportation,
   Groceries,
   Travel,
   Utilities,
   Entertainment,
   Housing,
   Education,
   Healthcare,
   Others,
   Deposit
}

val categoryToIconMap = mapOf(
   Category.Transportation to Icons.Default.DirectionsBus,
   Category.Groceries to Icons.Default.ShoppingCart,
   Category.Travel to Icons.Default.Map,
   Category.Utilities to Icons.Default.Phone,
   Category.Entertainment to Icons.Default.Movie,
   Category.Housing to Icons.Default.Apartment,
   Category.Education to Icons.Default.School,
   Category.Healthcare to Icons.Default.Spa,
   Category.Others to Icons.Default.Edit,
)
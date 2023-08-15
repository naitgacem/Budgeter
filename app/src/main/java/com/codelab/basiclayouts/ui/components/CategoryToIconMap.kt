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

val categoryToIconMap = mapOf(
   "Transportation" to Icons.Default.DirectionsBus,
   "Groceries" to Icons.Default.ShoppingCart,
   "Travel" to Icons.Default.Map,
   "Utilities" to Icons.Default.Phone,
   "Entertainment" to Icons.Default.Movie,
   "Housing" to Icons.Default.Apartment,
   "Education" to Icons.Default.School,
   "Healthcare" to Icons.Default.Spa,
   "Others" to Icons.Default.Edit,
)
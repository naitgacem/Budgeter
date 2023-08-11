package com.codelab.basiclayouts.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.ui.graphics.vector.ImageVector

data class Transaction(val icon: ImageVector = Icons.Filled.Adb,
                       val title: String,
                       val amount: Int,
                       val category: String = "Other"
)
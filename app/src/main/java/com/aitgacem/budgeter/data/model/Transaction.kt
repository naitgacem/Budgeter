package com.aitgacem.budgeter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aitgacem.budgeter.ui.components.Category

@Entity
data class Transaction(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "category") val category: Category,
)
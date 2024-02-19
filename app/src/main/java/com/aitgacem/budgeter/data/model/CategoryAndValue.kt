package com.aitgacem.budgeter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aitgacem.budgeter.ui.components.Category

@Entity
data class CategoryAndValue(
    @PrimaryKey var category: Category,
    @ColumnInfo var value: Double,
)
package com.aitgacem.budgeter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aitgacem.budgeter.ui.components.Category
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = "transactions",
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val amount: Double,
    val dateId: Long,
    val time: Long,
    val categoryId: Long
) : Parcelable

@Entity(tableName = "balance")
data class BalanceEntity(
    @PrimaryKey(autoGenerate = true) val dateId: Long,
    val date: Long,
    val balance: Double
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long,
    val name: Category,
    val total: Double,
    val month: Int,
    val year: Int,
)


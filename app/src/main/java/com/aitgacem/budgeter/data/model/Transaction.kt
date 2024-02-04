package com.aitgacem.budgeter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date


@Parcelize
data class Transaction(
    val id: Long,
    val title: String,
    val amount: Double,
    val date: Long,
    val time: Long,
    val category: Category
) : Parcelable

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
    val total: Double
)


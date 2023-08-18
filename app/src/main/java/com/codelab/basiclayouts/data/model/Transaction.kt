package com.codelab.basiclayouts.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "date") val dateAndTime: Long,
    @ColumnInfo(name = "title")    val title: String,
    @ColumnInfo(name = "amount")   val amount: Int,
    @ColumnInfo(name = "category") val category: String,

)
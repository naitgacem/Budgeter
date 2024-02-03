package com.aitgacem.budgeter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aitgacem.budgeter.data.model.CategoryEntity
import com.aitgacem.budgeter.data.model.BalanceEntity
import com.aitgacem.budgeter.data.model.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        BalanceEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionWithDetailsDao
    abstract fun balanceDao(): BalanceDao
    abstract fun categoryDao(): CategoryDao
}

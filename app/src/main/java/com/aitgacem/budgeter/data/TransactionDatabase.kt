package com.aitgacem.budgeter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aitgacem.budgeter.data.model.Balance
import com.aitgacem.budgeter.data.model.Transaction

@Database(entities = [Transaction::class, Balance::class], version = 2, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun balanceDao(): BalanceDao
}

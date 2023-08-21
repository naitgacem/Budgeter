package com.codelab.basiclayouts.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codelab.basiclayouts.data.model.Balance
import com.codelab.basiclayouts.data.model.Transaction

@Database(entities = [Transaction::class, Balance::class ], version = 2, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao
    abstract fun balanceDao() : BalanceDao
}

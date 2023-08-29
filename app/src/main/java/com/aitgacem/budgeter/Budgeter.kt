package com.aitgacem.budgeter

import android.app.Application
import androidx.room.Room
import com.aitgacem.budgeter.data.TransactionDatabase
import com.aitgacem.budgeter.data.TransactionsRepository

class Budgeter : Application() {
    lateinit var transactionsRepository: TransactionsRepository
    private lateinit var db: TransactionDatabase
    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            TransactionDatabase::class.java, "main_database"
        )
            .fallbackToDestructiveMigration()
            //.createFromAsset("database/prepackaged_database.db")
            .build()
        transactionsRepository = TransactionsRepository(db)

    }
}
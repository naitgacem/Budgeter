package com.codelab.basiclayouts

import android.app.Application
import androidx.room.Room
import com.codelab.basiclayouts.data.TransactionDatabase
import com.codelab.basiclayouts.data.TransactionsRepository

class Budgeter : Application() {
    lateinit var transactionsRepository: TransactionsRepository
    lateinit var db: TransactionDatabase
    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            TransactionDatabase::class.java, "main_database"
        ).allowMainThreadQueries().build()
        transactionsRepository = TransactionsRepository(db)

    }


}
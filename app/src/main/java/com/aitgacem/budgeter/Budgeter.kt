package com.aitgacem.budgeter

import android.app.Application
import androidx.room.Room
import com.aitgacem.budgeter.data.TransactionDatabase
import com.aitgacem.budgeter.data.TransactionsRepository

class Budgeter : Application() {
    lateinit var transactionsRepository: TransactionsRepository
    lateinit var db: TransactionDatabase
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

// C:\Users\Admin\AndroidStudioProjects\Budgeter\app\src\main\java\com\aitgacem\budgeter\Budgeter.kt
// C:\Users\Admin\AndroidStudioProjects\Budgeter\app\src\main\AndroidManifest.xml

}
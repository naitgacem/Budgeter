package com.codelab.basiclayouts

import android.app.Application
import com.codelab.basiclayouts.data.TransactionsRepository

class Budgeter : Application() {
    lateinit var transactionsRepository: TransactionsRepository

    override fun onCreate() {
        super.onCreate()
        transactionsRepository= TransactionsRepository(this)
    }

}
package com.codelab.basiclayouts.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelab.basiclayouts.data.model.Transaction

class TransactionsRepository(private val db: TransactionDatabase) {
    private var _transactionAddedEvent = MutableLiveData<Unit>()
    val transactionAddedEvent: LiveData<Unit> = _transactionAddedEvent

    private val transactionDao = db.transactionDao()

    fun writeTransactionToDatabase(transaction: Transaction){
        transactionDao.insertAll(transaction)
        _transactionAddedEvent.value = Unit
    }
    fun readAllTransactionsFromDatabase(): List<Transaction>{
        return db.transactionDao().getAll()
    }

}
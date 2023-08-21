package com.codelab.basiclayouts.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelab.basiclayouts.data.model.Balance
import com.codelab.basiclayouts.data.model.Transaction
import java.util.Calendar

class TransactionsRepository(private val db: TransactionDatabase) {
    private var _transactionAddedEvent = MutableLiveData<Unit>()
    val transactionAddedEvent: LiveData<Unit> = _transactionAddedEvent

    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()

    fun writeTransactionToDatabase(transaction: Transaction) {
        transactionDao.insert(transaction)
        val predecessor = balanceDao.loadPredecessor(transaction.date, transaction.id)
        var previousAmount = predecessor?.amount ?: 0

        if(predecessor == null){
            balanceDao.insert(
            Balance(transaction.id, transaction.date, transaction.amount)
            )
            previousAmount = transaction.amount
        } else {
            balanceDao.insert(
                Balance(transaction.id, transaction.date, predecessor.amount + transaction.amount )
            )
            previousAmount = transaction.amount + predecessor.amount
        }

        val newerBalances = balanceDao.loadNewerThan(
            transaction.date, transaction.id
        )

        for (balance in newerBalances.reversed()) {
            previousAmount += (transactionDao.loadTransaction(balance.id)).amount
            balanceDao.updateBalance(balance.copy(amount = previousAmount ))
        }
        _transactionAddedEvent.value = Unit
    }

    fun readAllTransactionsFromDatabase(): List<Transaction> {
        return db.transactionDao().getAll()
    }

    fun readBalance(): Int {
        return balanceDao.loadLastBalance() ?: 0
    }

    fun readBalance(transaction: Transaction): Int {
        return balanceDao.loadBalance(transaction.id)
    }

    fun readRecentTransactionsFromDatabase(): List<Transaction> {
        val oneWeekAgo = Calendar.getInstance()
        oneWeekAgo.add(Calendar.DAY_OF_WEEK, -7)
        return db.transactionDao().loadNewerThan(oneWeekAgo.timeInMillis)
    }

    fun loadTransaction(id: Long): Transaction {
        return db.transactionDao().loadTransaction(id)
    }

}
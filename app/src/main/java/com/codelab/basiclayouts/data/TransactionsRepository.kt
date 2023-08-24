package com.codelab.basiclayouts.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelab.basiclayouts.data.model.Balance
import com.codelab.basiclayouts.data.model.Transaction
import java.util.Calendar

class TransactionsRepository(private val db: TransactionDatabase) {
    private var _transactionAddedEvent = MutableLiveData<Unit>()
    val transactionAddedEvent: LiveData<Unit> = _transactionAddedEvent

    private var _balanceUpdatedEvent = MutableLiveData<Unit>()
    val balanceUpdatedEvent: LiveData<Unit> = _balanceUpdatedEvent


    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()

    fun writeTransactionToDatabase(transaction: Transaction) {
        transactionDao.insert(transaction)
        updateBalance(transaction)
        _transactionAddedEvent.postValue(Unit)
    }

    private fun updateBalance(
        transaction: Transaction,
    ) {
        var predecessor = balanceDao.loadPredecessor(date = transaction.date, id = transaction.id)
            ?.amount ?: 0

        balanceDao.insert(
            Balance(transaction.id, transaction.date, predecessor + transaction.amount)
        )
        predecessor += transaction.amount

        val newerBalances = balanceDao.loadNewerThan(
            transaction.date, transaction.id
        )

        for (balance in newerBalances) {
            predecessor += (transactionDao.loadTransaction(balance.id)).amount
            balanceDao.updateBalance(balance.copy(amount = predecessor))
        }
        _balanceUpdatedEvent.postValue(Unit)
    }

    fun readAllTransactionsFromDatabase(): List<Transaction> {
        return db.transactionDao().getAll()
    }

    fun readBalance(): Int {
        return balanceDao.loadLastBalance() ?: 0
    }

    fun readBalance(id: Long): Int {
        return balanceDao.loadBalance(id)
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
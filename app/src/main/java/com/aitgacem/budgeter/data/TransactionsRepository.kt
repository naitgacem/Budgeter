package com.aitgacem.budgeter.data

import androidx.annotation.WorkerThread
import com.aitgacem.budgeter.data.model.Balance
import com.aitgacem.budgeter.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

@WorkerThread
class TransactionsRepository(private val db: TransactionDatabase) {

    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()


    suspend fun writeTransactionToDatabase(transaction: Transaction) {
        transactionDao.insert(transaction)
        updateBalance(transaction)
    }

    private suspend fun updateBalance(
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
            predecessor += (transactionDao.loadTransaction(balance.id))?.amount ?: 0
            balanceDao.updateBalance(balance.copy(amount = predecessor))
        }
    }

    suspend fun readAllTransactionsFromDatabase(): Flow<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            db.transactionDao().getAll()
        }
    }

    suspend fun readBalance(): Flow<Int?> {
        return withContext(Dispatchers.IO) {
            balanceDao.loadLastBalance()
        }
    }

    suspend fun readBalance(id: Long): Int {
        return balanceDao.loadBalance(id)
    }

    suspend fun readRecentTransactionsFromDatabase(): Flow<List<Transaction>> {
        val oneWeekAgo = Calendar.getInstance()
        oneWeekAgo.add(Calendar.DAY_OF_WEEK, -7)
        return withContext(Dispatchers.IO) {
            db.transactionDao().loadNewerThan(oneWeekAgo.timeInMillis)
        }
    }

    suspend fun loadTransaction(id: Long): Transaction? {
        return db.transactionDao().loadTransaction(id)
    }

}
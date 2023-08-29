package com.aitgacem.budgeter.data

import androidx.annotation.WorkerThread
import com.aitgacem.budgeter.data.model.Balance
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

@WorkerThread
class TransactionsRepository(private val db: TransactionDatabase) {

    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()
    private val analyticsDao = db.analyticsDao()

    suspend fun writeTransactionToDatabase(transaction: Transaction) {
        transactionDao.insert(transaction)
        udpateCategoryAndValue(transaction)
        updateBalance(transaction)
    }

    private suspend fun updateBalance(
        transaction: Transaction,
    ) {
        var predecessor = balanceDao.loadPredecessor(date = transaction.date, id = transaction.id)
            ?.amount ?: 0.toFloat()

        val amount =
            if (transaction.category == Category.Deposit) transaction.amount else transaction.amount.unaryMinus()
        balanceDao.insert(
            Balance(transaction.id, transaction.date, predecessor + amount)
        )
        predecessor += amount

        val newerBalances = balanceDao.loadNewerThan(
            transaction.date, transaction.id
        )

        for (balance in newerBalances) {
            val loadedTransaction = transactionDao.loadTransaction(balance.id)

            if (loadedTransaction != null) {
                if (loadedTransaction.category == Category.Deposit) {
                    predecessor += loadedTransaction.amount
                } else {
                    predecessor -= loadedTransaction.amount
                }
            }
            balanceDao.updateBalance(balance.copy(amount = predecessor))
        }
    }

    suspend fun readAllTransactionsFromDatabase(): Flow<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            db.transactionDao().getAll()
        }
    }

    suspend fun readBalance(): Flow<Float?> {
        return withContext(Dispatchers.IO) {
            balanceDao.loadLastBalance()
        }
    }

    suspend fun readBalance(id: Long): Float {
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

    fun getCategoryAndValue(): Flow<List<CategoryAndValue>> {
        return analyticsDao.getAllData()
    }

    private suspend fun udpateCategoryAndValue(transaction: Transaction) {
        if (transaction.category == Category.Deposit) {
            return
        }
        val old = analyticsDao.getCategoryAmount(category = transaction.category.name)

        if (old == null) {
            analyticsDao.insert(
                CategoryAndValue(
                    transaction.category,
                    transaction.amount
                )
            )
        } else {
            analyticsDao.updateCategoryAmount(
                old.copy(
                    value = old.value + transaction.amount
                )
            )
        }
    }

}
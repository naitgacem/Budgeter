package com.aitgacem.budgeter.data

import androidx.annotation.WorkerThread
import androidx.room.withTransaction
import com.aitgacem.budgeter.data.model.Balance
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.DateAndBalance
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
        db.withTransaction {
            transactionDao.insert(transaction)
            updateCategoryAndValue(transaction)
            updateBalance(transaction)
        }
    }

    suspend fun updateTransaction(transaction: Transaction, oldTransaction: Transaction?) {
        db.withTransaction {
            transactionDao.update(transaction)
            if (oldTransaction == null) {
                updateCategoryAndValue(transaction)
            } else {
                updateCategoryAndValue(transaction, oldTransaction)
            }
            updateBalance(transaction, oldTransaction?.amount)
        }
    }

    private suspend fun updateBalance(
        transaction: Transaction,
        oldTransactionValue: Float? = null,
    ) {
        var predecessor =
            balanceDao.loadPredecessor(date = transaction.date, id = transaction.id)?.amount
                ?: 0.toFloat()

        val amount =
            if (transaction.category == Category.Deposit) transaction.amount else transaction.amount.unaryMinus()

        if (oldTransactionValue != null) {
            balanceDao.updateBalance(
                Balance(transaction.id, transaction.date, predecessor + amount)
            )
        } else {
            balanceDao.insert(
                Balance(transaction.id, transaction.date, predecessor + amount)
            )
        }

        predecessor += amount

        val newerBalances = balanceDao.loadNewerThan(
            transaction.date, transaction.id
        )

        for (balance in newerBalances) {
            val loadedTransaction = transactionDao.loadTransaction(balance.id)
            require(loadedTransaction != null)
            if (loadedTransaction.category == Category.Deposit) {
                predecessor += loadedTransaction.amount
            } else {
                predecessor -= loadedTransaction.amount
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

    fun getBalanceByDate(): Flow<List<DateAndBalance>> {
        return balanceDao.getBalanceByDate()
    }

    private suspend fun updateCategoryAndValue(
        transaction: Transaction,
    ) {
        if (transaction.category == Category.Deposit) {
            return
        }
        val oldCategoryAndValue =
            analyticsDao.getCategoryAmount(category = transaction.category.name)
        if (oldCategoryAndValue != null) {
            analyticsDao.updateCategoryAmount(
                oldCategoryAndValue.copy(
                    value = oldCategoryAndValue.value + transaction.amount
                )
            )
        } else {
            analyticsDao.insert(
                CategoryAndValue(
                    transaction.category, transaction.amount
                )
            )
        }
    }

    private suspend fun updateCategoryAndValue(
        transaction: Transaction,
        oldTransaction: Transaction,
    ) {
        if (transaction.category == Category.Deposit) {
            return
        }
        val oldEntry =
            analyticsDao.getCategoryAmount(category = oldTransaction.category.name)
        require(oldEntry != null) //it already exists since we are updating

        if (oldTransaction.category == transaction.category) {
            val diff = transaction.amount - oldTransaction.amount
            analyticsDao.updateCategoryAmount(
                oldEntry.copy(
                    value = oldEntry.value + diff
                )
            )
        } else {
            val newEntry =
                analyticsDao.getCategoryAmount(category = transaction.category.name)
            analyticsDao.updateCategoryAmount(
                oldEntry.copy(value = oldEntry.value - transaction.amount)
            )

            if (newEntry != null) {
                analyticsDao.updateCategoryAmount(
                    oldEntry.copy(
                        value = newEntry.value + transaction.amount
                    )
                )
            } else {
                analyticsDao.insert(
                    CategoryAndValue(
                        transaction.category, transaction.amount
                    )
                )
            }
        }
    }
}

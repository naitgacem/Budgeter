package com.aitgacem.budgeter.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.aitgacem.budgeter.data.model.BalanceEntity
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.CategoryEntity
import com.aitgacem.budgeter.data.model.TransactionEntity
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType.Date
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import java.util.Calendar


@WorkerThread
class TransactionsRepository(private val db: TransactionDatabase) {

    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()
    private val categoryDao = db.categoryDao()

    suspend fun writeTransactionToDatabase(transaction: Transaction) {
        val signedAmount = getSignedAmount(transaction)
        db.withTransaction {

        val balanceEntity = balanceDao.getBalanceEntityOnDay(transaction.date)
            val prevBalance = balanceDao.getBalanceEntityAtDay(transaction.date)?.balance ?: 0.0
            val newBalance = BalanceEntity(0, transaction.date, prevBalance + signedAmount)
            val balanceId: Long = when (balanceEntity) {
                null -> balanceDao.insert(newBalance)

                else -> {
                    balanceDao.updateBalance(newBalance.copy(dateId = balanceEntity.dateId))
                    balanceEntity.dateId
                }
            }
            //--------------------------------------------------------------------------
            val catId: Long = updateCatOnInsert(transaction)
            //----------------------------------------------------------------------------------------

            transactionDao.insert(
                TransactionEntity(
                    id = 0,
                    title = transaction.title,
                    amount = transaction.amount,
                    dateId = balanceId,
                    time = transaction.time,
                    categoryId = catId
                )
            )
            // Now we need to update the balance for all days after this one
            val daysAfter = balanceDao.getDayBalancesAfterDate(transaction.date)
            updateBalances(daysAfter, signedAmount)
        }
    }


    suspend fun updateTransaction(transaction: Transaction, oldTransaction: Transaction) {
        val signedOldTransactionAmount = getSignedAmount(oldTransaction)

        db.withTransaction {
            //Remove from old day
            val daysAfterOld = balanceDao.getDayBalancesStarting(oldTransaction.date)
            updateBalances(daysAfterOld, -1 * signedOldTransactionAmount)

            //Add to new day
            val signedAmount = getSignedAmount(transaction)
            val balanceEntity = balanceDao.getBalanceEntityOnDay(transaction.date)

            val prevBalance = balanceDao.getBalanceEntityAtDay(transaction.date)?.balance ?: 0.0
            val newBalance = BalanceEntity(0, transaction.date, prevBalance + signedAmount)

            val balanceId: Long = when (balanceEntity) {
                null -> balanceDao.insert(newBalance)
                else -> {
                    balanceDao.updateBalance(newBalance.copy(dateId = balanceEntity.dateId))
                    balanceEntity.dateId
                }
            }
            val catId: Long = updateCatOnUpdate(oldTransaction, transaction)

            transactionDao.updateTransaction(
                TransactionEntity(
                    oldTransaction.id,
                    transaction.title,
                    transaction.amount,
                    balanceId,
                    transaction.time,
                    catId
                )
            )

            // Now we need to update the balance for all days after this one
            val daysAfterNew = balanceDao.getDayBalancesAfterDate(transaction.date)
            updateBalances(daysAfterNew, signedAmount)
        }
    }

    private suspend fun updateCatOnInsert(transaction: Transaction): Long {
        val catEntity = categoryDao.getCategoryAmount(transaction.category)
        val prevAmount = catEntity?.total ?: 0.0

        val newCat = CategoryEntity(0, transaction.category, transaction.amount + prevAmount)

        val catId: Long = when (catEntity) {
            null -> categoryDao.insert(newCat)

            else -> {
                categoryDao.update(newCat.copy(categoryId = catEntity.categoryId))
                catEntity.categoryId
            }
        }
        return catId
    }

    private suspend fun updateCatOnUpdate(
        oldTransaction: Transaction,
        transaction: Transaction
    ): Long {
        //Remove from old category
        val oldCatValue =
            categoryDao.getCategoryAmount(oldTransaction.category)!! //has to exist
        categoryDao.update(
            oldCatValue.copy(
                total = oldCatValue.total - oldTransaction.amount
            )
        )
        //Add to new category
        return updateCatOnInsert(transaction)
    }


    private fun getSignedAmount(transaction: Transaction): Double {
        return if (transaction.category == Category.Deposit) {
            transaction.amount
        } else {
            -1 * transaction.amount
        }
    }

    private suspend fun updateBalances(
        balances: List<BalanceEntity>,
        signedAmount: Double
    ) {
        for (day in balances) {
            balanceDao.updateBalance(
                day.copy(
                    balance = day.balance + signedAmount
                )
            )
        }
    }


    fun readAllTransactionsFromDatabase(): LiveData<List<Transaction>> {
        return transactionDao.getTransactions()
    }

    suspend fun loadTransaction(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }

    suspend fun readBalance(date: Long): Double {
        return balanceDao.getBalanceEntityAtDay(date)?.balance ?: 0.0
    }

    fun readLatestBalance(): LiveData<Double?> {
        return balanceDao.getLatestBalance()
    }

    suspend fun loadCategoryTotal(category: Category): Double {
        return categoryDao.getCategoryAmount(category)?.total ?: 0.0
    }

    fun readRecentTransactionsFromDatabase(): LiveData<List<Transaction>> {
        val oneWeekAgo = Calendar.getInstance()
        oneWeekAgo.add(Calendar.DAY_OF_WEEK, -7)
        return transactionDao.loadNewerThan(oneWeekAgo.timeInMillis)
    }

    fun getCategoryAndValue(): LiveData<List<CategoryAndValue>> {
        return categoryDao.getCatAndValue()
    }

    fun getDayAndTransactions(): LiveData<Map<Date, List<Transaction>>> {
        return transactionDao.getDayTransactions()
    }

    fun getDayAndBalance(): LiveData<Map<Long, Double>> {
        return balanceDao.getBalanceChartData()
    }

    fun getDailyBalance(start: Long, end: Long): LiveData<Map<Long, Double>> {
        return balanceDao.getBalanceBetween(start, end)
    }

    fun getSpending(): LiveData<List<CategoryAndValue>> {
        return categoryDao.getCatAndValue()
    }

}

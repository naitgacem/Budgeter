package com.aitgacem.budgeter.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.aitgacem.budgeter.data.model.BalanceEntity
import com.aitgacem.budgeter.data.model.CategoryEntity
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.data.model.TransactionEntity

@WorkerThread
class TransactionsRepository(private val db: TransactionDatabase) {

    private val transactionDao = db.transactionDao()
    private val balanceDao = db.balanceDao()
    private val categoryDao = db.categoryDao()

    suspend fun writeTransactionToDatabase(transaction: Transaction) {
        db.withTransaction {
            //-----------------------------------------------------------------------------------
            val balanceEntity = balanceDao.getBalanceEntityOnDay(transaction.date)

            val prevBalance = balanceDao.getBalanceEntityBeforeDay(transaction.date)?.balance ?: 0.0
            val newBalance = BalanceEntity(0, transaction.date, prevBalance + transaction.amount)

            val balanceId: Long = when (balanceEntity) {
                null -> {
                    balanceDao.insert(newBalance)
                }

                else -> {
                    balanceDao.updateBalance(newBalance.copy(dateId = balanceEntity.dateId))
                    balanceEntity.dateId
                }
            }

            //--------------------------------------------------------------------------
            val catEntity = categoryDao.getCategoryAmount(transaction.category)
            val prevAmount = catEntity?.total ?: 0.0

            val newCat = CategoryEntity(0, transaction.category, transaction.amount + prevAmount)

            val catId: Long = when (catEntity) {
                null -> {
                    categoryDao.insert(newCat)
                }

                else -> {
                    categoryDao.update(newCat.copy(categoryId = catEntity.categoryId))
                    catEntity.categoryId
                }
            }
            //----------------------------------------------------------------------------------------

            transactionDao.insert(
                TransactionEntity(
                    0, transaction.title, transaction.amount, balanceId, transaction.time, catId
                )
            )

            // Now we need to update the balance for all days after this one
            val daysAfter = balanceDao.getDayBalancesAfterDate(transaction.date)
            for (day in daysAfter) {
                balanceDao.updateBalance(
                    day.copy(
                        balance = transaction.amount + day.balance
                    )
                )
            }
        }
    }

    suspend fun updateTransaction(transaction: Transaction, oldTransaction: Transaction) {
        db.withTransaction {
            // Remove from old day
            val oldBalance =
                balanceDao.getBalanceEntityBeforeDay(oldTransaction.date)!! //has to exist
            balanceDao.updateBalance(
                oldBalance.copy(
                    balance = oldBalance.balance - oldTransaction.amount
                )
            )
            //Add to new day
            val balanceEntity = balanceDao.getBalanceEntityOnDay(transaction.date)

            val prevBalance = balanceDao.getBalanceEntityBeforeDay(transaction.date)?.balance ?: 0.0
            val newBalance = BalanceEntity(0, transaction.date, prevBalance + transaction.amount)

            val balanceId: Long = when (balanceEntity) {
                null -> {
                    balanceDao.insert(newBalance)
                }

                else -> {
                    balanceDao.updateBalance(newBalance.copy(dateId = balanceEntity.dateId))
                    balanceEntity.dateId
                }
            }
            //Remove from old category
            val oldCatValue =
                categoryDao.getCategoryAmount(oldTransaction.category)!! //has to exist
            categoryDao.update(
                oldCatValue.copy(
                    total = oldCatValue.total - oldTransaction.amount
                )
            )
            //Add to new category
            val catEntity = categoryDao.getCategoryAmount(transaction.category)
            val prevAmount = catEntity?.total ?: 0.0

            val newCat = CategoryEntity(0, transaction.category, transaction.amount + prevAmount)

            val catId: Long = when (catEntity) {
                null -> {
                    categoryDao.insert(newCat)
                }

                else -> {
                    categoryDao.update(newCat.copy(categoryId = catEntity.categoryId))
                    catEntity.categoryId
                }
            }
            transactionDao.updateTransaction(
                TransactionEntity(
                    transaction.id,
                    transaction.title,
                    transaction.amount,
                    balanceId,
                    transaction.time,
                    catId
                )
            )

            // Now we need to update the balance for all days after this one
            val daysAfter = balanceDao.getDayBalancesAfterDate(transaction.date)
            for (day in daysAfter) {
                balanceDao.updateBalance(
                    day.copy(
                        balance = transaction.amount + day.balance
                    )
                )
            }
        }
    }

    suspend fun readAllTransactionsFromDatabase(): LiveData<List<Transaction>> {
        TODO()
    }
////    suspend fun readAllTransactionsFromDatabase(): Flow<List<Transaction>> {
////        return withContext(Dispatchers.IO) {
////            db.transactionDao().getAll()
////        }
////    }
//
////    suspend fun readBalance(): Flow<Float?> {
//////        return withContext(Dispatchers.IO) {
//////            balanceDao.loadLastBalance()
//////        }
////    }
////
////    suspend fun readBalance(id: Long): Float {
////        // return balanceDao.loadBalance(id)
////    }
//
////    suspend fun readRecentTransactionsFromDatabase(): Flow<List<Transaction>> {
////        val oneWeekAgo = Calendar.getInstance()
////        oneWeekAgo.add(Calendar.DAY_OF_WEEK, -7)
////        return withContext(Dispatchers.IO) {
////            db.transactionDao().loadNewerThan(oneWeekAgo.timeInMillis)
////        }
////    }
//
////    suspend fun loadTransaction(id: Long): Transaction? {
////        return db.transactionDao().loadTransaction(id)
////    }
//
////    fun getCategoryAndValue(): Flow<List<CategoryAndValue>> {
////        //return analyticsDao.getAllData()
////    }
////
////    fun getBalanceByDate(): Flow<List<DateAndBalance>> {
////        //return balanceDao.getBalanceByDate()
////    }
//
////    private suspend fun updateCategoryAndValue(
////        transaction: Transaction,
////    ) {
////        if (transaction.category == Category.Deposit) {
////            return
////        }
////        val oldCategoryAndValue =
////            analyticsDao.getCategoryAmount(category = transaction.category.name)
////        if (oldCategoryAndValue != null) {
////            analyticsDao.updateCategoryAmount(
////                oldCategoryAndValue.copy(
////                    value = oldCategoryAndValue.value + transaction.amount
////                )
////            )
////        } else {
////            analyticsDao.insert(
////                CategoryAndValue(
////                    transaction.category, transaction.amount
////                )
////            )
////        }
////    }
//
//    private suspend fun updateCategoryAndValue(
//        transaction: Transaction,
//        oldTransaction: Transaction,
//    ) {
////        if (transaction.category == Category.Deposit) {
////            return
////        }
////        val oldEntry = analyticsDao.getCategoryAmount(category = oldTransaction.category.name)
////        require(oldEntry != null) //it already exists since we are updating
////
////        if (oldTransaction.category == transaction.category) {
////            val diff = transaction.amount - oldTransaction.amount
////            analyticsDao.updateCategoryAmount(
////                oldEntry.copy(
////                    value = oldEntry.value + diff
////                )
////            )
////        } else {
////            val newEntry = analyticsDao.getCategoryAmount(category = transaction.category.name)
////            analyticsDao.updateCategoryAmount(
////                oldEntry.copy(value = oldEntry.value - transaction.amount)
////            )
////
////            if (newEntry != null) {
////                analyticsDao.updateCategoryAmount(
////                    newEntry.copy(
////                        value = newEntry.value + transaction.amount
////                    )
////                )
////            } else {
////                analyticsDao.insert(
////                    CategoryAndValue(
////                        transaction.category, transaction.amount
////                    )
////                )
////            }
////        }
//    }
}

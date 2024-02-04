package com.aitgacem.budgeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.data.model.TransactionEntity

@Dao
interface TransactionWithDetailsDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transactionWithDetails: TransactionEntity)

    @Update
    suspend fun updateTransaction(updatedTransaction: TransactionEntity)


    @Query(
        "SELECT transactions.id, transactions.title, transactions.amount, balance.date as date, transactions.time, categories.name as category " +
                "FROM transactions " +
                "INNER JOIN categories ON transactions.categoryId = categories.categoryId " +
                "INNER JOIN balance ON transactions.dateId = balance.dateId "
    )
    fun getTransactions(): LiveData<List<Transaction>>

    @Query(
        "SELECT transactions.id, transactions.title, transactions.amount, balance.date as date, transactions.time, categories.name as category " +
                "FROM transactions " +
                "INNER JOIN categories ON transactions.categoryId = categories.categoryId " +
                "INNER JOIN balance ON transactions.dateId = balance.dateId " +
                "WHERE transactions.id = :transactionId"
    )
    suspend fun getTransactionById(transactionId: Long): Transaction?

    @Query(
        "SELECT transactions.id, transactions.title, transactions.amount, balance.date as date, transactions.time, categories.name as category " +
                "FROM transactions " +
                "INNER JOIN categories ON transactions.categoryId = categories.categoryId " +
                "INNER JOIN balance ON transactions.dateId = balance.dateId " +
                "WHERE date <= :date"
    )
    fun loadNewerThan(date: Long): LiveData<List<Transaction>>
}

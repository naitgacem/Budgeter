package com.aitgacem.budgeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.TransactionEntity
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.components.ItemType.*

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
                "INNER JOIN balance ON transactions.dateId = balance.dateId " +
                "order by date desc"
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
                "WHERE date >= :date"
    )
    fun loadNewerThan(date: Long): LiveData<List<Transaction>>

    @Query(
        "SELECT balance.date, transactions.id, transactions.title, transactions.amount, " +
                "transactions.time, categories.name as category " +
                "from balance " +
                "join transactions on balance.dateId = transactions.dateId " +
                "join categories on categories.categoryId == transactions.categoryId " +
                "order by date desc"
    )
    fun getDayTransactions(): LiveData<Map<@MapColumn(columnName = "date") Date, List<Transaction>>>
}

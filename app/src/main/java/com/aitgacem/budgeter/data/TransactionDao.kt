package com.aitgacem.budgeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.data.model.TransactionEntity

@Dao
interface TransactionWithDetailsDao {

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transactionWithDetails: TransactionEntity)

    @Update
    suspend fun updateTransaction(updatedTransaction: TransactionEntity)


    @Query(
        "SELECT transactions.id, transactions.title, transactions.amount, transactions.time, transactions.dateId as date, categories.name as category " +
                "FROM transactions " +
                "INNER JOIN categories ON transactions.categoryId = categories.categoryId"
    )
    suspend fun getTransactions(): List<Transaction>
}

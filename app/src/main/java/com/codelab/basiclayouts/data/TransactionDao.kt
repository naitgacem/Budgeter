package com.codelab.basiclayouts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.codelab.basiclayouts.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` order by date desc, id desc")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` where id = :id ")
    suspend fun loadTransaction(id: Long): Transaction?

    @Query("SELECT * FROM `transaction` where date >= :date order by date desc, id desc")
    fun loadNewerThan(date: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` where (date > :date) or (date == :date and id > :id)")
    fun loadNewerThan(date: Long, id: Long): Flow<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}

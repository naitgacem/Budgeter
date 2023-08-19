package com.codelab.basiclayouts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.codelab.basiclayouts.data.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` order by date desc")
    fun getAll(): List<Transaction>
    @Query("SELECT * FROM `transaction` where id = :id ")
    fun loadTransaction(id: String): Transaction
    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)
}

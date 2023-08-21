package com.codelab.basiclayouts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.codelab.basiclayouts.data.model.Balance

@Dao
interface BalanceDao {
    @Query("SELECT * FROM `balance` order by date desc")
    fun getAll(): List<Balance>

    @Query("SELECT * FROM `balance` where id = :id ")
    fun loadBalance(id: String): Balance

    @Query("SELECT * FROM `balance` where (date == :date and id < :id) or (date < :date) " +
            "order by date desc, id desc limit 1")
    fun loadPredecessor(date: Long, id: Long): Balance?

    @Query("select * from `balance` where (date > :date) or (date == :date and id > :id) ")
    fun loadNewerThan(date: Long, id: Long): List<Balance>

    @Query("select amount from `balance` order by date desc,  id desc limit 1")
    fun loadLastBalance() : Int?

    @Query("select amount from `balance` where id = :id")
    fun loadBalance(id: Long) : Int

    @Insert
    fun insert(balance: Balance)

    @Update
    fun updateBalance(balance: Balance)
    @Delete
    fun delete(balance: Balance)
}
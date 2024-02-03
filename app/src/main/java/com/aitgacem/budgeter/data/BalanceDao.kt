package com.aitgacem.budgeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.BalanceEntity

@Dao
interface BalanceDao {

    @Insert
    suspend fun insert(balance: BalanceEntity): Long

    @Update
    suspend fun updateBalance(balance: BalanceEntity)

    @Delete
    suspend fun delete(balance: BalanceEntity)

    @Query("select * from balance where date = :date")
    suspend fun getBalanceEntityOnDay(date: Long): BalanceEntity?

    @Query("select * from balance where date <= :date order by date asc limit 1")
    suspend fun getBalanceEntityBeforeDay(date: Long): BalanceEntity?
}
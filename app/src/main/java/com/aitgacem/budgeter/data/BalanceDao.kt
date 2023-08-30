package com.aitgacem.budgeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.Balance
import com.aitgacem.budgeter.data.model.DateAndBalance
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Query("SELECT * FROM `balance` order by date desc")
    fun getAll(): Flow<List<Balance>>

    @Query("SELECT * FROM `balance` where id = :id ")
    suspend fun loadBalance(id: String): Balance

    @Query(
        "SELECT * FROM `balance` where (date == :date and id < :id) or (date < :date) " +
                "order by date desc, id desc limit 1"
    )
    suspend fun loadPredecessor(date: Long, id: Long): Balance?

    @Query(
        "select * from `balance` where (date > :date) or (date == :date and id > :id) " +
                "order by date asc, id asc"
    )
    suspend fun loadNewerThan(date: Long, id: Long): List<Balance>

    @Query("select amount from `balance` order by date desc,  id desc limit 1")
    fun loadLastBalance(): Flow<Float?>

    @Query("select amount from `balance` where id = :id")
    suspend fun loadBalance(id: Long): Float

    @Query(
        "WITH dateAndMaxId AS (" +
                "SELECT date, MAX(id) AS max_id " +
                "FROM balance " +
                "GROUP BY date" +
                ") " +
                "SELECT b1.date, b1.amount " +
                "FROM balance b1 " +
                "JOIN dateAndMaxId b2 ON b1.id = b2.max_id"
    )
    fun getBalanceByDate(): Flow<List<DateAndBalance>>

    @Insert
    suspend fun insert(balance: Balance)

    @Update
    suspend fun updateBalance(balance: Balance)

    @Delete
    suspend fun delete(balance: Balance)
}
package com.aitgacem.budgeter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.CategoryAndValue
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalyticsDao {
    @Query("select * from `categoryandvalue` order by value desc")
    fun getAllData(): Flow<List<CategoryAndValue>>

    @Query("select * from `categoryandvalue` where category = :category")
    suspend fun getCategoryAmount(category: String): CategoryAndValue?

    @Insert
    suspend fun insert(categoryAndValue: CategoryAndValue)

    @Update
    suspend fun updateCategoryAmount(categoryAndValue: CategoryAndValue)
}
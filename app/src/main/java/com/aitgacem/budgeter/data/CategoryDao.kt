package com.aitgacem.budgeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.CategoryEntity
import com.aitgacem.budgeter.ui.components.Category

@Dao
interface CategoryDao {
    @Query("select * from categories where name  = :category and month = :month and year = :year")
    suspend fun getCategoryAmount(category: Category, month: Int, year: Int): CategoryEntity?

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity): Long

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Query("SELECT name as category, total as value from categories where month = :month and year = :year")
    fun getCatAndValue(month: Int, year: Int): LiveData<List<CategoryAndValue>>

    @Query("SELECT * from categories")
    fun getDump(): LiveData<List<CategoryEntity>>
}
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
    @Query("select * from categories where name  = :category")
    suspend fun getCategoryAmount(category: Category): CategoryEntity?

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity): Long

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Query("SELECT name as category, total as value from categories")
    fun getCatAndValue(): LiveData<List<CategoryAndValue>>
}
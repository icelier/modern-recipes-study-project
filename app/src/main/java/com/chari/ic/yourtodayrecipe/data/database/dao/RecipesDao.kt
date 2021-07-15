package com.chari.ic.yourtodayrecipe.data.database.dao

import androidx.room.*
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun findAll(): Flow<List<RecipeEntity>>
}
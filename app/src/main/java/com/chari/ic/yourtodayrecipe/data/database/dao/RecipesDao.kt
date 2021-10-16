package com.chari.ic.yourtodayrecipe.data.database.dao

import androidx.room.*
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.FoodJokeEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun findAllRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouriteRecipe: FavouritesEntity)

    @Delete
    suspend fun deleteFavouriteRecipe(favouriteRecipe: FavouritesEntity)

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    fun findAllFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Query("DELETE FROM favourites")
    suspend fun deleteAllFavouriteRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJoke: FoodJokeEntity)

    @Query("SELECT * FROM food_joke ORDER BY id ASC")
    fun findAllFoodJokes(): Flow<List<FoodJokeEntity>>
}
package com.chari.ic.yourtodayrecipe.data.data_source

import android.util.Log
import com.chari.ic.yourtodayrecipe.data.database.dao.RecipesDao
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.FoodJokeEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipesDao
) {

    fun findAllRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.findAllRecipes()
    }

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    fun findAllFavouriteRecipes(): Flow<List<FavouritesEntity>> {
        val favouriteFromDb = recipeDao.findAllFavouriteRecipes()
        Log.d("LocalDataSource", "favouriteFromDb == null: ${favouriteFromDb == null}")

        return favouriteFromDb
//        return recipeDao.findAllFavouriteRecipes()
    }

    suspend fun insertFavouriteRecipe(favouriteRecipe: FavouritesEntity) {
        recipeDao.insertFavouriteRecipe(favouriteRecipe)
    }

    suspend fun deleteFavouriteRecipe(favouriteRecipe: FavouritesEntity) {
        recipeDao.deleteFavouriteRecipe(favouriteRecipe)
    }

    suspend fun deleteAllFavouriteRecipes() {
        recipeDao.deleteAllFavouriteRecipes()
    }

    fun findAllFoodJokes(): Flow<List<FoodJokeEntity>> {
        return recipeDao.findAllFoodJokes()
    }

    suspend fun insertFoodJoke(foodJoke: FoodJokeEntity) {
        recipeDao.insertFoodJoke(foodJoke)
    }
}
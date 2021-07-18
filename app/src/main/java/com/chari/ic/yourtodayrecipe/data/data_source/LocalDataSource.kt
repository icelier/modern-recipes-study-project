package com.chari.ic.yourtodayrecipe.data.data_source

import com.chari.ic.yourtodayrecipe.data.database.dao.RecipesDao
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
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
        return recipeDao.findAllFavouriteRecipes()
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
}
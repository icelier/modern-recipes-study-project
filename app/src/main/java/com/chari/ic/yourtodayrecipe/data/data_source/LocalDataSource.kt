package com.chari.ic.yourtodayrecipe.data.data_source

import com.chari.ic.yourtodayrecipe.data.database.dao.RecipesDao
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipesDao
) {

    fun findAll(): Flow<List<RecipeEntity>> {
        return recipeDao.findAll()
    }

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }
}
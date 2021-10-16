package com.chari.ic.yourtodayrecipe.data.data_source

import com.chari.ic.yourtodayrecipe.data.network.FoodRecipesApi
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeResponse> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<RecipeResponse> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}
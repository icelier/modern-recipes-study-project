package com.chari.ic.yourtodayrecipe.data

import com.chari.ic.yourtodayrecipe.data.network.FoodRecipesApi
import com.chari.ic.yourtodayrecipe.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }
}
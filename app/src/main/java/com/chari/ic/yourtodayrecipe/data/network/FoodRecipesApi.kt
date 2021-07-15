package com.chari.ic.yourtodayrecipe.data.network

import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val TAG = "FoodRecipesApi"
interface FoodRecipesApi {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<RecipeResponse>
}
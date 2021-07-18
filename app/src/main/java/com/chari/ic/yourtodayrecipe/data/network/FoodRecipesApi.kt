package com.chari.ic.yourtodayrecipe.data.network

import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<RecipeResponse>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<RecipeResponse>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String
    ): Response<FoodJoke>
}
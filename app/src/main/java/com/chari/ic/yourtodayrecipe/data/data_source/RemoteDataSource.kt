package com.chari.ic.yourtodayrecipe.data.data_source

import android.util.Log
import com.chari.ic.yourtodayrecipe.data.network.FoodRecipesApi
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "RemoteDataSource"
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeResponse> {
        Log.d(TAG, "RemoteDataSource: getRecipes")
        return foodRecipesApi.getRecipes(queries)
    }
}
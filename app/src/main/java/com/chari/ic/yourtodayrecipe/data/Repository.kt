package com.chari.ic.yourtodayrecipe.data

import com.chari.ic.yourtodayrecipe.data.data_source.LocalDataSource
import com.chari.ic.yourtodayrecipe.data.data_source.RemoteDataSource
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeResponse> {
        return remoteDataSource.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<RecipeResponse> {
        return remoteDataSource.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return remoteDataSource.getFoodJoke(apiKey)
    }
}
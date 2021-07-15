package com.chari.ic.yourtodayrecipe.data

import android.util.Log
import com.chari.ic.yourtodayrecipe.data.data_source.LocalDataSource
import com.chari.ic.yourtodayrecipe.data.data_source.RemoteDataSource
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "Repository"
@ActivityRetainedScoped
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
) {
//    val remote = remoteDataSource
//    val local = localDataSource

    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeResponse> {
        Log.d(TAG, "Repository: getRecipes")
        return remoteDataSource.getRecipes(queries)
    }
}
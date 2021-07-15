package com.chari.ic.yourtodayrecipe

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.chari.ic.yourtodayrecipe.data.Repository
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
private const val TAG = "RecipeViewModel"
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    /* ROOM DATABASE */
    val cachedRecipes: LiveData<List<RecipeEntity>> =
        repository.localDataSource.findAll().asLiveData()

    private fun insert(recipe: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertRecipe(recipe)
        }
    }

    /* RETROFIT NETWORK FETCHED DATA */
    private val recipeResponse: MutableLiveData<NetworkResult<RecipeResponse>> = MutableLiveData()
    val recipeSearchResult: LiveData<NetworkResult<RecipeResponse>> = recipeResponse

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "Internet connection present")
                val response = repository.getRecipes(queries)
                Log.d(TAG, "Handle response")
                recipeResponse.value = handleFoodRecipeResponse(response)
                val data = recipeResponse.value!!.data
                if (data != null) {
                    offlineCacheData(data)
                }
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipeResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }

    private fun offlineCacheData(data: RecipeResponse) {
        val recipeEntity = RecipeEntity(data)
        insert(recipeEntity)
    }

    private fun handleFoodRecipeResponse(response: Response<RecipeResponse>): NetworkResult<RecipeResponse> {
        Log.d(TAG, "response: ${response.code()} and ${response.body()}")
        return when {
            response.message().toString().contains("timeout") ->
                NetworkResult.Error("Timeout")
            response.code() == 402 ->
                NetworkResult.Error("API key incorrect")
            response.body()!!.results.isNullOrEmpty() ->
                NetworkResult.Error("Recipes not found")
            response.isSuccessful -> {
                val foodRecipes = response.body()!!
                NetworkResult.Success(foodRecipes)
            }
            else -> NetworkResult.Error(response.message())
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        if (connectivityManager == null) {
            return false
        }
        val isNetworkActive = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
            val capabilities: NetworkCapabilities = connectivityManager.getNetworkCapabilities(
                connectivityManager.activeNetwork
            ) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork?.type == ConnectivityManager.TYPE_WIFI ||
                activeNetwork?.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
            false
        }

        return isNetworkActive
    }
}
package com.chari.ic.yourtodayrecipe

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chari.ic.yourtodayrecipe.data.Repository
import com.chari.ic.yourtodayrecipe.model.FoodRecipe
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    val recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handleFoodRecipeResponse(response)
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipeResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
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

    fun hasInternetConnection(): Boolean {
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



    }
}
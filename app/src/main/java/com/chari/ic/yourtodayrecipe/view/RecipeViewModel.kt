package com.chari.ic.yourtodayrecipe.view

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.chari.ic.yourtodayrecipe.data.DataStoreRepository
import com.chari.ic.yourtodayrecipe.data.Repository
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.FoodJokeEntity
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.chari.ic.yourtodayrecipe.util.Constants
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "RecipeViewModel"
@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
): AndroidViewModel(application) {

    var networkStatus = false
    var backOnline = false

    /** DATASTORE */
    private var mealType = Constants.DEFAULT_MEAL_TYPE
    private var dietType = Constants.DEFAULT_DIET_TYPE

    val storedMealAndDietTypes = dataStoreRepository.readMealAndDietTypes()

    fun saveMealAndDietTypes(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.writeMealAndDietTypes(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    val storedBackOnline = dataStoreRepository.readBackOnline()

    private fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.writeBackOnline(backOnline)
        }
    }

    /** ROOM DATABASE */
    val cachedRecipes: LiveData<List<RecipeEntity>> =
        repository.localDataSource.findAllRecipes().asLiveData()

    private fun insertRecipesResponse(recipesResponse: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertRecipe(recipesResponse)
        }
    }

    val cachedFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.localDataSource.findAllFavouriteRecipes().asLiveData()

    fun insertFavouriteRecipe(favouriteRecipe: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertFavouriteRecipe(favouriteRecipe)
        }
    }

    fun deleteFavouriteRecipe(favouriteRecipe: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteFavouriteRecipe(favouriteRecipe)
        }
    }

    fun deleteAllFavouriteRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteAllFavouriteRecipes()
        }
    }

    val cachedFoodJokes: LiveData<List<FoodJokeEntity>> =
        repository.localDataSource.findAllFoodJokes().asLiveData()

    private fun insertFoodJoke(foodJoke: FoodJokeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertFoodJoke(foodJoke)
        }
    }

    /** RETROFIT NETWORK FETCHED DATA */
    private val recipesResponse: MutableLiveData<NetworkResult<RecipeResponse>> = MutableLiveData()
    val recipesResult: LiveData<NetworkResult<RecipeResponse>> = recipesResponse

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "Internet connection present")
                val response = repository.getRecipes(queries)
                Log.d(TAG, "Handle response")
                recipesResponse.value = handleFoodRecipeResponse(response)
                val data = recipesResponse.value!!.data
                if (data != null) {
                    offlineCacheData(data)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes fetch failed")
            }
        } else {
            recipesResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }


    private val recipesSearchResponse: MutableLiveData<NetworkResult<RecipeResponse>> = MutableLiveData()
    val recipesSearch: LiveData<NetworkResult<RecipeResponse>> = recipesSearchResponse

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        recipesSearchResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "Internet connection present")
                val response = repository.searchRecipes(searchQuery)
                Log.d(TAG, "Handle response")
                recipesSearchResponse.value = handleFoodRecipeResponse(response)
            } catch (e: Exception) {
                recipesSearchResponse.value = NetworkResult.Error("Recipes fetch failed")
            }
        } else {
            recipesSearchResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }

    private val foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()
    val foodJoke: LiveData<NetworkResult<FoodJoke>> = foodJokeResponse

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "Internet connection present")
                val response = repository.getFoodJoke(apiKey)
                Log.d(TAG, "Handle response")
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value?.data
                if (foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("FoodJoke fetch failed")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        Log.d(TAG, "response: ${response.code()} and ${response.body()}")
        return when {
            response.message().toString().contains("timeout") ->
                NetworkResult.Error("Timeout")
            response.code() == 402 ->
                NetworkResult.Error("Probably, you have exhausted your attempts for today...")
            response.isSuccessful -> {
                val foodRecipes = response.body()!!
                NetworkResult.Success(foodRecipes)
            }
            else -> NetworkResult.Error(response.message())
        }
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    fun setupQuery(): HashMap<String, String> {
        val queries = HashMap<String, String>()

        Log.d(TAG, "in SetupQuery for new Query")
        viewModelScope.launch {
            storedMealAndDietTypes.collect { preferences ->
                mealType = preferences.selectedMealType
                dietType = preferences.selectedDietType
                Log.d(TAG, "Values for query collected from datastore: " +
                        "meal: ${mealType} and diet: $dietType")
            }
        }

        Log.d(TAG, "queries update started")
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        Log.d(TAG, "queries update finished")

        return queries
    }

    fun setupSearchQuery(searchQuery: String): HashMap<String, String> {
        val queries = HashMap<String, String>()
        Log.d(TAG, "in setupSearchQuery for new Search Query")

        queries[Constants.QUERY_TITLE] = searchQuery
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun offlineCacheData(data: RecipeResponse) {
        val recipeEntity = RecipeEntity(data)
        insertRecipesResponse(recipeEntity)
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
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else {
            if (backOnline) {
                Toast.makeText(getApplication(), "Back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}
package com.chari.ic.yourtodayrecipe.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.chari.ic.yourtodayrecipe.util.Constants
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.BACK_ONLINE
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.SEARCH_QUERY
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.SELECTED_DIET_TYPE
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.SELECTED_DIET_TYPE_ID
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.SELECTED_MEAL_TYPE
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.SELECTED_MEAL_TYPE_ID
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val selectedMealType = stringPreferencesKey(SELECTED_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(SELECTED_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(SELECTED_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(SELECTED_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(BACK_ONLINE)
        val searchQuery = stringPreferencesKey(SEARCH_QUERY)
    }

    suspend fun writeMealAndDietTypes(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedMealType] = mealType
            preferences[PreferencesKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKeys.selectedDietType] = dietType
            preferences[PreferencesKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun writeBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    suspend fun writeSearchQuery(searchQuery: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.searchQuery] = searchQuery
        }
    }

    fun readSearchQuery(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.searchQuery] ?: ""
        }

    fun readMealAndDietTypes(): Flow<MealAndDietPreferences> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val mealType =
                    preferences[PreferencesKeys.selectedMealType] ?: Constants.DEFAULT_MEAL_TYPE
                val mealTypeId = preferences[PreferencesKeys.selectedMealTypeId] ?: 0
                val dietType =
                    preferences[PreferencesKeys.selectedDietType] ?: Constants.DEFAULT_DIET_TYPE
                val dietTypeId = preferences[PreferencesKeys.selectedDietTypeId] ?: 0
                MealAndDietPreferences(mealType, mealTypeId, dietType, dietTypeId)
            }
    }

    fun readBackOnline(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val backOnline = preferences[PreferencesKeys.backOnline] ?: false
                backOnline
            }
    }
}

data class MealAndDietPreferences(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)
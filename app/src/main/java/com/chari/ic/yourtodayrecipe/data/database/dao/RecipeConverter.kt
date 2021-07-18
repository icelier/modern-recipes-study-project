package com.chari.ic.yourtodayrecipe.data.database.dao

import androidx.room.TypeConverter
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeConverter {
    private val gson = Gson()

    @TypeConverter
    fun recipeResponseToJson(recipeResponse: RecipeResponse): String {
        return gson.toJson(recipeResponse)
    }

    @TypeConverter
    fun jsonToRecipeResponse(json: String): RecipeResponse {
        val typeToken = object: TypeToken<RecipeResponse>() {}.type
        return gson.fromJson(json, typeToken)
    }

    @TypeConverter
    fun recipeToJson(recipe: Recipe): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun jsonToRecipe(json: String): Recipe {
        val typeToken = object: TypeToken<Recipe>() {}.type
        return gson.fromJson(json, typeToken)
    }

}
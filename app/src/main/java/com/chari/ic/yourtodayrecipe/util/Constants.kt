package com.chari.ic.yourtodayrecipe.util

class Constants {
    companion object {
        // api
        const val API_KEY = "e59b72018cb44b90ace600cdc981f597"
        const val BASE_URL = "https://api.spoonacular.com/"

        // database
        const val DATABASE_NAME = "recipes"
        const val TABLE_NAME = "recipes"

        // request base params
        const val QUERY_TITLE = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
    }
}
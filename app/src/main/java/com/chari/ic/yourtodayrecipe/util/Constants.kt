package com.chari.ic.yourtodayrecipe.util

class Constants {
    companion object {
        // api
        const val API_KEY = "e59b72018cb44b90ace600cdc981f597"
        const val BASE_URL = "https://api.spoonacular.com/"

        // database
        const val DATABASE_NAME = "recipes"
        const val TABLE_NAME = "recipes"

        // Bottom Sheet
        const val DEFAULT_MEAL_TYPE = "Main course"
        const val DEFAULT_DIET_TYPE = "Gluten Free"

        // request base params
        const val QUERY_TITLE = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val DEFAULT_QUERY_NUMBER = "50"

        // Data Store
        const val DATA_STORE_NAME = "recipes_preferences"
        const val SELECTED_MEAL_TYPE = "mealType"
        const val SELECTED_MEAL_TYPE_ID = "mealTypeId"
        const val SELECTED_DIET_TYPE = "dietType"
        const val SELECTED_DIET_TYPE_ID = "dietTypeId"
        const val BACK_ONLINE = "backIOnline"
        const val SEARCH_QUERY = "searchQuery"
    }
}
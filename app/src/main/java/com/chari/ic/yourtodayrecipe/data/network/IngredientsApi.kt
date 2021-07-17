package com.chari.ic.yourtodayrecipe.data.network

import com.chari.ic.yourtodayrecipe.model.Ingredient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface IngredientsApi {
    @GET("cdn/ingredients_100x100/{imageTitle}")
    suspend fun getIngredients(
        @QueryMap queries: HashMap<String, String>,
        @Path("imageTitle") imageTitle: String
    ): Response<Ingredient>
}
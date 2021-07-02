package com.chari.ic.yourtodayrecipe.model


import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("results")
    val results: List<RecipeDetails>
)
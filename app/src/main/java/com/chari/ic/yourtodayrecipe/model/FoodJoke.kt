package com.chari.ic.yourtodayrecipe.model

import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val joke: String
)
package com.chari.ic.yourtodayrecipe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    @SerializedName("amount")
    var amount: Double = 0.0,
    @SerializedName("consistency")
    var consistency: String = "",
    @SerializedName("image")
    var image: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("original")
    var original: String = "",
    @SerializedName("unit")
    var unit: String = ""
): Parcelable
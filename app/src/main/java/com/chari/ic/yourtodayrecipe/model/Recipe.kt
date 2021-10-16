package com.chari.ic.yourtodayrecipe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Recipe (
    @SerializedName("aggregateLikes")
    var aggregateLikes: Int = 0,
    @SerializedName("cheap")
    var cheap: Boolean = false,
    @SerializedName("dairyFree")
    var dairyFree: Boolean = false,
    @SerializedName("extendedIngredients")
    var extendedIngredients: @RawValue List<Ingredient> = emptyList(),
    @SerializedName("glutenFree")
    var glutenFree: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("readyInMinutes")
    var readyInMinutes: Int = 0,
    @SerializedName("sourceName")
    var sourceName: String?,
    @SerializedName("sourceUrl")
    var sourceUrl: String = "",
    @SerializedName("summary")
    var summary: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("vegan")
    var vegan: Boolean = false,
    @SerializedName("vegetarian")
    var vegetarian: Boolean = false,
    @SerializedName("veryHealthy")
    var veryHealthy: Boolean = false
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
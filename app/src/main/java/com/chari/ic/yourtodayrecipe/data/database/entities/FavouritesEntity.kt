package com.chari.ic.yourtodayrecipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.util.Constants

@Entity(tableName = Constants.FAVOURITES_TABLE_NAME)
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val favouriteRecipe: Recipe
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FavouritesEntity

        if (favouriteRecipe != other.favouriteRecipe) return false

        return true
    }

    override fun hashCode(): Int {
        return favouriteRecipe.hashCode()
    }
}
package com.chari.ic.yourtodayrecipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.util.Constants

@Entity(tableName = Constants.FAVOURITES_TABLE_NAME)
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val favouriteRecipe: Recipe
) {
}
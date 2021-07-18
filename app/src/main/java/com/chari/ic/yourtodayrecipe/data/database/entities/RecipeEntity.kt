package com.chari.ic.yourtodayrecipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.chari.ic.yourtodayrecipe.util.Constants

@Entity(tableName = Constants.RECIPES_TABLE_NAME)
data class RecipeEntity(
    val recipe: RecipeResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0L
}
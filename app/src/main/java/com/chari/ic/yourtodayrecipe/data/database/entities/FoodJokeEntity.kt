package com.chari.ic.yourtodayrecipe.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.util.Constants

@Entity(tableName = Constants.FOOD_JOKE_TABLE_NAME)
class FoodJokeEntity(
    @Embedded
    val foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
package com.chari.ic.yourtodayrecipe.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chari.ic.yourtodayrecipe.data.database.dao.RecipeConverter
import com.chari.ic.yourtodayrecipe.data.database.dao.RecipesDao
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1
)
@TypeConverters(RecipeConverter::class)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}
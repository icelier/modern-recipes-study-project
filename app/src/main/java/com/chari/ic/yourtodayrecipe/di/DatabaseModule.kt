package com.chari.ic.yourtodayrecipe.di

import android.content.Context
import androidx.room.Room
import com.chari.ic.yourtodayrecipe.data.database.RecipeDatabase
import com.chari.ic.yourtodayrecipe.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun getRecipesDao(database: RecipeDatabase) = database.recipesDao()

}
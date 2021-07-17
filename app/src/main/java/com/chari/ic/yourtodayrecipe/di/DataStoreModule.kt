package com.chari.ic.yourtodayrecipe.di

import android.content.Context
import com.chari.ic.yourtodayrecipe.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun getDataStore(@ApplicationContext context: Context) = context.dataStore
}
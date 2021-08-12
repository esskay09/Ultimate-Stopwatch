package com.terrranullius.stopwatch.di

import android.content.Context
import androidx.room.Room
import com.terrranullius.stopwatch.db.LapDatabase
import com.terrranullius.stopwatch.other.Constants.LAP_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideLapDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, LapDatabase::class.java, LAP_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLapDao(lapDatabase: LapDatabase) = lapDatabase.getLapDao()
}
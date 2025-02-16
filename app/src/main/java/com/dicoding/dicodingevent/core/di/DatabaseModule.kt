package com.dicoding.dicodingevent.core.di

import android.content.Context
import androidx.room.Room
import com.dicoding.dicodingevent.core.data.local.room.EventDao
import com.dicoding.dicodingevent.core.data.local.room.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase = Room.databaseBuilder(
        context,
        EventDatabase::class.java,
        "Event.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideEventDao(database: EventDatabase): EventDao = database.eventDao()
}
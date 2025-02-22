package com.dicoding.core.di

import android.content.Context
import androidx.room.Room
import com.dicoding.core.data.local.room.EventDao
import com.dicoding.core.data.local.room.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("event".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "Event.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }

    @Provides
    fun provideEventDao(database: EventDatabase): EventDao = database.eventDao()
}
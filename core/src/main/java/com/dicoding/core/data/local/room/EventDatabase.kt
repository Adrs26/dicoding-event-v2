package com.dicoding.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.core.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
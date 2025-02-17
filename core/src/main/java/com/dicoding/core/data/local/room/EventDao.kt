package com.dicoding.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.core.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY beginTime DESC")
    fun getAllEvent(): Flow<List<EventEntity>>

    @Query("SELECT COUNT(*) FROM event WHERE id = :id")
    fun isEventExist(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: EventEntity)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEvent(id: Int)
}
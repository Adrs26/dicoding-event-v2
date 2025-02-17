package com.dicoding.core.data.local

import com.dicoding.core.data.local.entity.EventEntity
import com.dicoding.core.data.local.room.EventDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val eventDao: EventDao) {
    fun getFavoriteEvents() = eventDao.getAllEvent()
    fun isEventExist(id: Int) = eventDao.isEventExist(id)
    suspend fun insertEvent(event: EventEntity) = eventDao.insertEvent(event)
    suspend fun deleteEvent(id: Int) = eventDao.deleteEvent(id)
}
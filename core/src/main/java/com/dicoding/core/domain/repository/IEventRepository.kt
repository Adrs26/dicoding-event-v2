package com.dicoding.core.domain.repository

import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    fun getUpcomingEvents(): Flow<ApiResponse<List<Event>>>
    fun getCompletedEvents(): Flow<ApiResponse<List<Event>>>
    fun getEventById(id: Int): Flow<ApiResponse<Event>>
    fun getEventsByKeyword(keyword: String): Flow<ApiResponse<List<Event>>>
    fun getFavoriteEvents(): Flow<List<Event>>
    fun isEventExist(id: Int): Flow<Int>
    suspend fun insertFavoriteEvent(event: Event)
    suspend fun deleteFavoriteEvent(id: Int)
}
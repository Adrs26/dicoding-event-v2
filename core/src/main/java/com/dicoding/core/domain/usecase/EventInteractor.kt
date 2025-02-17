package com.dicoding.core.domain.usecase

import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import com.dicoding.core.domain.repository.IEventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventInteractor @Inject constructor(private val eventRepository: IEventRepository) :
    EventUseCase {
    override fun getUpcomingEvents(): Flow<ApiResponse<List<Event>>> {
        return eventRepository.getUpcomingEvents()
    }

    override fun getCompletedEvents(): Flow<ApiResponse<List<Event>>> {
        return eventRepository.getCompletedEvents()
    }

    override fun getEventById(id: Int): Flow<ApiResponse<Event>> {
        return eventRepository.getEventById(id)
    }

    override fun getEventsByKeyword(keyword: String): Flow<ApiResponse<List<Event>>> {
        return eventRepository.getEventsByKeyword(keyword)
    }

    override fun getFavoriteEvents(): Flow<List<Event>> {
        return eventRepository.getFavoriteEvents()
    }

    override fun isEventExist(id: Int): Flow<Int> {
        return eventRepository.isEventExist(id)
    }

    override suspend fun insertFavoriteEvent(event: Event) {
        eventRepository.insertFavoriteEvent(event)
    }

    override suspend fun deleteFavoriteEvent(id: Int) {
        eventRepository.deleteFavoriteEvent(id)
    }
}
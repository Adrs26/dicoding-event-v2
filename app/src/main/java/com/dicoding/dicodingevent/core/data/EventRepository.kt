package com.dicoding.dicodingevent.core.data

import com.dicoding.dicodingevent.core.data.local.LocalDataSource
import com.dicoding.dicodingevent.core.data.remote.RemoteDataSource
import com.dicoding.dicodingevent.core.data.remote.network.ApiResponse
import com.dicoding.dicodingevent.core.domain.model.Event
import com.dicoding.dicodingevent.core.domain.repository.IEventRepository
import com.dicoding.dicodingevent.core.util.DataHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IEventRepository {
    override fun getUpcomingEvents(): Flow<ApiResponse<List<Event>>> {
        return remoteDataSource.getUpcomingEvents()
            .mapApiResponse(DataHelper::mapResponsesToDomain)
    }

    override fun getCompletedEvents(): Flow<ApiResponse<List<Event>>> {
        return remoteDataSource.getCompletedEvents()
            .mapApiResponse(DataHelper::mapResponsesToDomain)
    }

    override fun getEventById(id: Int): Flow<ApiResponse<Event>> {
        return remoteDataSource.getEventById(id)
            .mapApiResponse(DataHelper::mapResponseToDomain)
    }

    override fun getEventsByKeyword(keyword: String): Flow<ApiResponse<List<Event>>> {
        return remoteDataSource.getEventByKeyword(keyword)
            .mapApiResponse(DataHelper::mapResponsesToDomain)
    }

    override fun getFavoriteEvents(): Flow<List<Event>> {
        return localDataSource.getFavoriteEvents().map {
            DataHelper.mapEntitiesToDomain(it)
        }
    }

    override fun isEventExist(id: Int): Flow<Int> {
        return localDataSource.isEventExist(id)
    }

    override suspend fun insertFavoriteEvent(event: Event) {
        localDataSource.insertEvent(DataHelper.mapDomainToEntity(event))
    }

    override suspend fun deleteFavoriteEvent(id: Int) {
        localDataSource.deleteEvent(id)
    }

    private inline fun <T, R> Flow<ApiResponse<T>>.mapApiResponse(
        crossinline transform: (T) -> R
    ): Flow<ApiResponse<R>> {
        return map { response ->
            when (response) {
                is ApiResponse.Loading -> ApiResponse.Loading
                is ApiResponse.Success -> ApiResponse.Success(transform(response.data))
                is ApiResponse.Empty -> ApiResponse.Empty
                is ApiResponse.Error -> ApiResponse.Error(response.message)
            }
        }
    }
}
package com.dicoding.core.data.remote

import com.dicoding.core.data.remote.network.ApiService
import com.dicoding.core.data.remote.response.DetailEventResponse
import com.dicoding.core.data.remote.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    private inline fun <T> safeApiCall(crossinline apiCall: suspend () -> T): Flow<ApiResponse<T>> {
        return flow {
            val response = apiCall()
            when {
                response is List<*> && response.isEmpty() -> emit(ApiResponse.Empty)
                else -> emit(ApiResponse.Success(response))
            }
        }.onStart { emit(ApiResponse.Loading) }
            .catch { e -> emit(ApiResponse.Error(e.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    fun getUpcomingEvents(): Flow<ApiResponse<List<DetailEventResponse>>> {
        return safeApiCall { apiService.getEvents(1).events }
    }

    fun getCompletedEvents(): Flow<ApiResponse<List<DetailEventResponse>>> {
        return safeApiCall { apiService.getEvents(0).events }
    }

    fun getEventById(id: Int): Flow<ApiResponse<DetailEventResponse>> {
        return safeApiCall { apiService.getEventById(id).event }
    }

    fun getEventByKeyword(keyword: String): Flow<ApiResponse<List<DetailEventResponse>>> {
        return safeApiCall { apiService.getEventsByKeyword(-1, keyword).events }
    }
}
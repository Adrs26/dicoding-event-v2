package com.dicoding.core.data.remote.network

import com.dicoding.core.data.remote.response.ListEventResponse
import com.dicoding.core.data.remote.response.SingleEventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(@Query("active") active: Int): ListEventResponse

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): SingleEventResponse

    @GET("events")
    suspend fun getEventsByKeyword(
        @Query("active") active: Int,
        @Query("q") q: String
    ): ListEventResponse
}
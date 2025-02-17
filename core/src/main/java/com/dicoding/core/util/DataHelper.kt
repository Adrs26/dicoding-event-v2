package com.dicoding.core.util

import com.dicoding.core.data.local.entity.EventEntity
import com.dicoding.core.data.remote.response.DetailEventResponse
import com.dicoding.core.domain.model.Event

object DataHelper {
    var eventId: Int = 0
    var event: Event? = null

    fun mapResponsesToDomain(input: List<DetailEventResponse>): List<Event> =
        input.map {
            Event(
                id = it.id,
                name = it.name,
                description = it.description,
                imageLogo = it.imageLogo,
                mediaCover = it.mediaCover,
                category = it.category,
                ownerName = it.ownerName,
                cityName = it.cityName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                endTime = it.endTime,
                link = it.link
            )
        }

    fun mapResponseToDomain(input: DetailEventResponse): Event =
        Event(
            id = input.id,
            name = input.name,
            description = input.description,
            imageLogo = input.imageLogo,
            mediaCover = input.mediaCover,
            category = input.category,
            ownerName = input.ownerName,
            cityName = input.cityName,
            quota = input.quota,
            registrants = input.registrants,
            beginTime = input.beginTime,
            endTime = input.endTime,
            link = input.link
        )

    fun mapEntitiesToDomain(input: List<EventEntity>): List<Event> =
        input.map {
            Event(
                id = it.id,
                name = it.name,
                description = it.description,
                imageLogo = it.imageLogo,
                mediaCover = it.mediaCover,
                category = it.category,
                ownerName = it.ownerName,
                cityName = it.cityName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                endTime = it.endTime,
                link = it.link
            )
        }

    fun mapDomainToEntity(input: Event): EventEntity =
        EventEntity(
            id = input.id,
            name = input.name,
            description = input.description,
            imageLogo = input.imageLogo,
            mediaCover = input.mediaCover,
            category = input.category,
            ownerName = input.ownerName,
            cityName = input.cityName,
            quota = input.quota,
            registrants = input.registrants,
            beginTime = input.beginTime,
            endTime = input.endTime,
            link = input.link
        )
}
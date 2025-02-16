package com.dicoding.dicodingevent.core.di

import com.dicoding.dicodingevent.core.data.EventRepository
import com.dicoding.dicodingevent.core.domain.repository.IEventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(eventRepository: EventRepository): IEventRepository
}
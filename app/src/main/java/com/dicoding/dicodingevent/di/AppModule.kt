package com.dicoding.dicodingevent.di

import com.dicoding.core.domain.usecase.EventInteractor
import com.dicoding.core.domain.usecase.EventUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun provideEventUseCase(eventInteractor: EventInteractor): EventUseCase
}
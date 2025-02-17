package com.dicoding.dicodingevent.di

import com.dicoding.core.domain.usecase.EventUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {
    fun eventUseCase(): EventUseCase
}
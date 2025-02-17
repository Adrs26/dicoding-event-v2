package com.dicoding.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.core.domain.usecase.EventUseCase

class FavoriteViewModel(eventUseCase: EventUseCase) : ViewModel() {
    val favoriteEvents = eventUseCase.getFavoriteEvents().asLiveData()
}
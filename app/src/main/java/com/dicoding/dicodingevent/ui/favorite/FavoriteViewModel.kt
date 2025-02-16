package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.dicodingevent.core.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(eventUseCase: EventUseCase) : ViewModel() {
    val favoriteEvents = eventUseCase.getFavoriteEvents().asLiveData()
}
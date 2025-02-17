package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import com.dicoding.core.domain.usecase.EventUseCase
import com.dicoding.core.util.DataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val eventUseCase: EventUseCase) : ViewModel() {
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    init {
        viewModelScope.launch {
            eventUseCase.isEventExist(DataHelper.eventId).collectLatest { status ->
                _isFavorite.value = status > 0
            }
        }
    }

    val eventDetail: StateFlow<ApiResponse<Event>> =
        eventUseCase.getEventById(DataHelper.eventId)
            .stateIn(viewModelScope, SharingStarted.Lazily, ApiResponse.Loading)

    fun insertFavoriteEvent() = viewModelScope.launch {
        eventUseCase.insertFavoriteEvent(DataHelper.event!!)
    }

    fun deleteFavoriteEvent() = viewModelScope.launch {
        eventUseCase.deleteFavoriteEvent(DataHelper.eventId)
    }
}
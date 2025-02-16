package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.core.data.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.core.domain.usecase.EventUseCase
import com.dicoding.dicodingevent.core.data.remote.network.ApiResponse
import com.dicoding.dicodingevent.core.domain.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(eventUseCase: EventUseCase) : ViewModel() {
    val upcomingEvents: StateFlow<ApiResponse<List<Event>>> =
        eventUseCase.getUpcomingEvents()
            .stateIn(viewModelScope, SharingStarted.Lazily, ApiResponse.Loading)
}
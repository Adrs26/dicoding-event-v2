package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import com.dicoding.core.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CompletedViewModel @Inject constructor(eventUseCase: EventUseCase) : ViewModel() {
    val completedEvents: StateFlow<ApiResponse<List<Event>>> =
        eventUseCase.getCompletedEvents()
            .stateIn(viewModelScope, SharingStarted.Lazily, ApiResponse.Loading)
}
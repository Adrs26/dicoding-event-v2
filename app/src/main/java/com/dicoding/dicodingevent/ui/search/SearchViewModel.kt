package com.dicoding.dicodingevent.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.core.data.remote.network.ApiResponse
import com.dicoding.dicodingevent.core.domain.model.Event
import com.dicoding.dicodingevent.core.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val eventUseCase: EventUseCase) : ViewModel() {
    private val _searchResults = MutableStateFlow<ApiResponse<List<Event>>>(ApiResponse.Empty)
    val searchResults: StateFlow<ApiResponse<List<Event>>> = _searchResults

    fun searchEvents(keyword: String) {
        viewModelScope.launch {
            eventUseCase.getEventsByKeyword(keyword).collectLatest { state ->
                _searchResults.value = state
            }
        }
    }
}

package com.example.cs3200firebasestarter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs3200firebasestarter.ui.model.SearchResponse
import com.example.cs3200firebasestarter.ui.repositories.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SpotifyViewModel(private val spotifyRepository: SpotifyRepository) : ViewModel() {

    // StateFlow to hold search results and expose them to the UI
    private val _searchResults = MutableStateFlow<Result<SearchResponse>?>(null)
    val searchResults: StateFlow<Result<SearchResponse>?> = _searchResults

    // Function to perform a search
    fun searchTracks(query: String) {
        viewModelScope.launch {
            val result = spotifyRepository.searchTracks(query)
            _searchResults.value = result
        }
    }

    // Additional functions for other Spotify API interactions can be added here
}
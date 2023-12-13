package com.example.cs3200firebasestarter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class PostCreationViewModel : ViewModel() {
    val userName = MutableLiveData("")
    val userLocation = MutableLiveData("Fetching location...") // Initially set to a placeholder
    // TODO: Add Spotify API data handling

    // Function to update the user's location
    fun updateUserLocation(location: String) {
        userLocation.value = location
    }

    // TODO: Function to fetch data from Spotify API
}

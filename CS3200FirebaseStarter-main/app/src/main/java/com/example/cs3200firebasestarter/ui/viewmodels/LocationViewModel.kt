package com.example.cs3200firebasestarter.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import java.util.*
import java.io.IOException
import android.util.Log
import android.location.Location
import com.google.android.gms.location.*

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    val locationData = MutableLiveData<String>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.locations?.firstOrNull()?.let { location ->
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val locality = address.locality ?: "Unknown city"
                        val adminArea = address.adminArea ?: "Unknown state"
                        locationData.value = "$locality, $adminArea"
                    } else {
                        locationData.value = "Address not found"
                    }
                } catch (e: IOException) {
                    locationData.value = "Geocoder failed: ${e.message}"
                }
            } ?: run {
                locationData.value = "Location is null"
            }
        }
    }

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
            )
        } catch (unlikely: SecurityException) {
            locationData.value = "Lost location permission. Could not request updates."
        }
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}


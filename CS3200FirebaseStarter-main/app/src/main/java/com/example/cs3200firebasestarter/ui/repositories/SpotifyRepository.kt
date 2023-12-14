package com.example.cs3200firebasestarter.ui.repositories

import com.example.cs3200firebasestarter.ui.authentication.AuthStateManager
import com.example.cs3200firebasestarter.ui.model.SearchResponse
import com.example.cs3200firebasestarter.ui.network.ApiClient
import com.example.cs3200firebasestarter.ui.network.SpotifyService

class SpotifyRepository(private val spotifyService: SpotifyService) {

    suspend fun searchTracks(query: String): Result<SearchResponse> {
        return try {
            // Make the API call using Retrofit
            val response = spotifyService.searchTracks(query)
            // Check if the response is successful
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success(responseBody)
                } else {
                    Result.failure(RuntimeException("Response body is null"))
                }
            } else {
                Result.failure(RuntimeException("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Handle exceptions like network errors
            Result.failure(e)
        }
    }

    // Add more methods here for other Spotify API endpoints
}

// You might also want a factory function or a dependency injection setup to create instances of SpotifyRepository
object SpotifyRepositoryFactory {
    fun create(authStateManager: AuthStateManager): SpotifyRepository {
        val spotifyService = ApiClient.createSpotifyService(authStateManager)
        return SpotifyRepository(spotifyService)
    }
}

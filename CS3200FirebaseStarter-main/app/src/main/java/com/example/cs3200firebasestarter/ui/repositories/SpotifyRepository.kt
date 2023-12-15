package com.example.cs3200firebasestarter.ui.repositories

import com.example.cs3200firebasestarter.ui.model.SearchResponse
import com.example.cs3200firebasestarter.ui.network.SpotifyService

class SpotifyRepository(private val spotifyService: SpotifyService) {

    suspend fun searchTracks(query: String): Result<SearchResponse> {
        return try {
            val response = spotifyService.searchTracks(query)
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
            Result.failure(e)
        }
    }

}


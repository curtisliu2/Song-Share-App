package com.example.cs3200firebasestarter.ui.network
import com.example.cs3200firebasestarter.ui.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyService {
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track"
    ): Response<SearchResponse>
}
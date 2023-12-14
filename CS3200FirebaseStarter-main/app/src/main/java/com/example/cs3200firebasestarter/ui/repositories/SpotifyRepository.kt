package com.example.cs3200firebasestarter.ui.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// Define your interfaces for the Spotify API
interface SpotifyApi {
    @POST("/token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Header("Authorization") authorization: String
    ): retrofit2.Call<SpotifyTokenResponse>

    @GET("/search")
    suspend fun searchSpotify(
        @Query("q") query: String,
        @Query("type") type: String,
        @Header("Authorization") authorization: String
    ): retrofit2.Call<SpotifySearchResponse>
}

// Spotify Repository for handling API calls
object SpotifyRepository {
    // These should be securely fetched from a secure source
    val clientId = "58762570f97643eeb21bd3c140f3e58f"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val spotifyService: SpotifyApi= retrofit.create(SpotifyApi::class.java)

    private val tokenRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://accounts.spotify.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tokenService: SpotifyApi = tokenRetrofit.create(SpotifyApi::class.java)
}

// Data models to match the JSON response from Spotify
data class SpotifyTokenResponse(val access_token: String)
data class SpotifySearchResponse(val tracks: TracksResponse)
data class TracksResponse(val items: List<TrackItem>, val total: Int, val limit: Int, val offset: Int, val href: String, val previous: String?, val next: String?)
data class TrackItem(val album: Album, val artists: List<Artist>, val available_markets: List<String>, val disc_number: Int, val duration_ms: Int, val explicit: Boolean, val external_urls: SpotifyExternalURL, val href: String, val id: String, val is_local: Boolean, val name: String, val popularity: Int, val preview_url: String?, val track_number: Int, val type: String, val uri: String)
data class Album(val album_type: String, val artists: List<Artist>, val available_markets: List<String>, val external_urls: SpotifyExternalURL, val href: String, val id: String, val images: List<Image>, val name: String, val release_date: String, val release_date_precision: String, val total_tracks: Int, val type: String, val uri: String)
data class Artist(
    val external_urls: SpotifyExternalURL,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class SpotifyExternalURL(
    val spotify: String // URL to the Spotify page for the artist, track, or album
)

data class Image(
    val height: Int, // Height of the image in pixels
    val url: String,  // Direct URL to the image
    val width: Int    // Width of the image in pixels
)
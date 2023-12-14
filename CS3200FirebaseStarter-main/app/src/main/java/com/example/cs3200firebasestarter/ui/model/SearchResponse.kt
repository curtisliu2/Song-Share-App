package com.example.cs3200firebasestarter.ui.model
import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "tracks") val tracks: TracksResponse
)

data class TracksResponse(
    @Json(name = "items") val items: List<TrackItem>
)

data class TrackItem(
    @Json(name = "name") val name: String,
    @Json(name = "artists") val artists: List<Artist>,
    // ... other relevant fields ...
)

data class Artist(
    @Json(name = "name") val name: String
    // ... other relevant fields ...
)

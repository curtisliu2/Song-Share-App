package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.cs3200firebasestarter.ui.model.TrackItem
import com.example.cs3200firebasestarter.ui.viewmodels.SpotifyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(spotifyViewModel: SpotifyViewModel) {
    var searchText by remember { mutableStateOf("") }
    val searchResults by spotifyViewModel.searchResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search Songs") },
            trailingIcon = {
                IconButton(onClick = { spotifyViewModel.searchTracks(searchText) }) {
                    Icon(Icons.Filled.Search, "Search")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Search button
        Button(
            onClick = { spotifyViewModel.searchTracks(searchText) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        searchResults?.let { result ->
            result.onSuccess { response ->
                response.tracks.items.forEach { track ->
                    SearchResultItem(track)
                }
            }.onFailure { throwable ->
                Text("Error: ${throwable.localizedMessage}")
            }
        }
    }
}

@Composable
fun SearchResultItem(track: TrackItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = track.name)
            Text(text = "Artists: ${track.artists.joinToString { it.name }}")
        }
    }
}

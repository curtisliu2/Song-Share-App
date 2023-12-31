package com.example.cs3200firebasestarter.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs3200firebasestarter.ui.viewmodels.LocationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationScreen(navHostController: NavHostController) {
    val locationViewModel: LocationViewModel = viewModel()
    val location by locationViewModel.locationData.observeAsState()
    val userEmail by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email) }
    var userName = userEmail?.substringBefore("@")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            locationViewModel.startLocationUpdates()
        } else {
            locationViewModel.locationData.value = "Permission denied"
        }
    }
    var songLibrary by remember {
        mutableStateOf(listOf<String>())
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.assets.open("My Spotify Library.txt").bufferedReader().useLines { lines ->
            songLibrary = lines.toList()
        }
    }


    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()


    var searchText by remember { mutableStateOf("") }
    val filteredItems = if (searchText.isNotEmpty()) {
        songLibrary.map { song -> Pair(song, countConsecutiveMatches(song, searchText)) }
            .filter { it.second > 0 } // Filter out items with no matches
            .sortedByDescending { it.second } // Sort by match count
            .map { it.first } // Extract the song names
            .take(5)
    } else {
        listOf()
    }

//    var userName by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround

    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Create Post",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        location?.let { loc ->
            Text(
                text = "Location: $loc",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Form fields
        userName?.let { OutlinedTextField(value = it, onValueChange = { userName = it }, label = { Text("Username") }) }
        OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") })
        // TODO Spotify stuff here
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Song") },
            modifier = Modifier.fillMaxWidth()  // Ensure full width
        )

        LazyColumn (
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()  // Ensure full width
        ) {
            items(filteredItems) { item ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { searchText = item }  // Clickable and autofill searchText
                ) {
                    Text(text = item, modifier = Modifier.padding(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
        // Save button
        Button(onClick = {
            val newCharacter = hashMapOf(
                "userName" to userName,
                "location" to location,
                "caption" to caption,
                "searchText" to searchText,
                )
            db.collection("posts").add(newCharacter)
            navHostController.navigate("home")
        }) {
            Text("Post")
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}

fun countConsecutiveMatches(str: String, searchText: String): Int {
    var maxMatch = 0
    for (i in str.indices) {
        var matchCount = 0
        for (j in searchText.indices) {
            if (i + j < str.length && str[i + j].equals(searchText[j], ignoreCase = true)) {
                matchCount++
            } else {
                break
            }
        }
        maxMatch = maxOf(maxMatch, matchCount)
    }
    return maxMatch
}

package com.example.cs3200firebasestarter.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs3200firebasestarter.ui.viewmodels.LocationViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationScreen(navHostController: NavHostController) {
    val locationViewModel: LocationViewModel = viewModel()
    val location by locationViewModel.locationData.observeAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            locationViewModel.startLocationUpdates()
        } else {
            locationViewModel.locationData.value = "Permission denied"
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()


    var searchText by remember { mutableStateOf("") }
    val allItems = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew") // Example data
    val filteredItems = allItems.filter { it.startsWith(searchText, ignoreCase = true) }.take(5) // Filtering and limiting to 5 items

    // State variables for each attribute
    var userName by remember { mutableStateOf("") }
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
        Spacer(modifier = Modifier.height(150.dp))
        // Form fields
        OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text("Username") })
        OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") })
        // TODO Spotify stuff here
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Song") }
        )
        LazyColumn (
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filteredItems) { item ->
                Text(text = item)
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
                // spotify to spotify

                )
            db.collection("posts").add(newCharacter)
            navHostController.navigate("home")
        }) {
            Text("Post")
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}
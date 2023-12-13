package com.example.cs3200firebasestarter.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

    // State variables for each attribute
    var userName by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround

    ) {
        Text(
            text = "Create Post",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        // Form fields
        OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text("Username") })
        OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") })
        location?.let { loc ->
            Text(
                text = loc,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        // TODO Spotify stuff here

        // Save button
        Button(onClick = {
            val newCharacter = hashMapOf(
                "userName" to userName,
                "location" to location,
                "caption" to caption,
                // spotify to spotify

                )
            db.collection("posts").add(newCharacter)
            navHostController.navigate("home")
        }) {
            Text("Post")
        }
    }
}
package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.cs3200firebasestarter.ui.viewmodels.LocationViewModel
import android.Manifest;
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.livedata.observeAsState
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun HomeScreen(navHostController: NavHostController) {
    val context = LocalContext.current
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
    val db = FirebaseFirestore.getInstance()
    var postsWithIds by remember { mutableStateOf(listOf<Triple<String, String, Map<String, Any>>>()) }
    LaunchedEffect(Unit) {
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                val fetchedPosts = mutableListOf<Triple<String, String, Map<String, Any>>>()
                for (document in result) {
                    val name = document.getString("userName") ?: ""
                    val additionalData = document.data - "userName"  // Assuming you have other fields like 'race' and 'class' here
                    fetchedPosts.add(Triple(name, document.id, additionalData))
                }
                postsWithIds = fetchedPosts
            }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Your Feed!",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        LazyColumn {
//            modifier = Modifier.padding(vertical = 8.dp)
            items(postsWithIds) { (userName, id, additionalData) ->
                val caption = additionalData["caption"] as? String ?: ""
                val location = additionalData["location"] as? String?: ""
               // val spotify = additionalData["class"] as? String ?: ""
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "$userName - $caption - $location ") //include spotify here
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        location?.let { loc ->
            Text(
                text = loc,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

}
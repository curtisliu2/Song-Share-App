package com.example.cs3200firebasestarter.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs3200firebasestarter.ui.viewmodels.LocationViewModel
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

    fun deletePost(postId: String) {
        db.collection("posts").document(postId).delete()
            .addOnSuccessListener {
                // Filter out the deleted post from the list and update the UI
                postsWithIds = postsWithIds.filterNot { it.second == postId }
            }
            .addOnFailureListener {
                // Handle failure (e.g., show a toast message)
            }
    }


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
    ) {
        Text(
            text = "Your Feed!",
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
        Spacer(modifier = Modifier.height(50.dp))
        LazyColumn {
            // Assuming postsWithIds is your data source
            items(postsWithIds) { (userName, id, additionalData) ->
                val caption = additionalData["caption"] as? String ?: ""
                val location = additionalData["location"] as? String ?: ""
                val searchText = additionalData["searchText"] as? String?: ""

                // Using Card for a boxed appearance
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = searchText,
                            fontSize = 25.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Shared from: $location",
                            fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "$userName: $caption",
                            fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(20.dp))
                        TextButton(onClick = { deletePost(id) }) {
                            Text("Delete Post")
                        }
                    }
                }
            }
        }

    }
}


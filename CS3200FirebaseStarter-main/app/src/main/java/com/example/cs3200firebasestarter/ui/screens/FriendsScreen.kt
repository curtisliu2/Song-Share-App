package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button



@Composable
fun FriendsScreen(navHostController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    var friendsWithIds by remember { mutableStateOf(listOf<Triple<String, String, Map<String, Any>>>()) }

    // Fetch friends from Firestore
    LaunchedEffect(Unit) {
        db.collection("friends")
            .get()
            .addOnSuccessListener { result ->
                val fetchedFriends = mutableListOf<Triple<String, String, Map<String, Any>>>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val additionalData = document.data - "name"  // Assuming you have other fields like 'race' and 'class' here
                    fetchedFriends.add(Triple(name, document.id, additionalData))
                }
                friendsWithIds = fetchedFriends
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Friends List",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        // Display friends
        LazyColumn {
            items(friendsWithIds) { (name, id, additionalData) ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "$name")
//                    Button(onClick = {
//                        navHostController.navigate("characterEdit/$id")
//                    }) {
//                        Text("Edit")
//                    }
                }
            }
        }
    }
}
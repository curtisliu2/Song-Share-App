package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun FriendsScreen(navHostController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    var friendsWithIds by remember { mutableStateOf(listOf<Pair<String, Map<String, Any>>>()) }

    fun deleteFriend(friendId: String) {
        db.collection("friends").document(friendId).delete()
            .addOnSuccessListener {
                friendsWithIds = friendsWithIds.filterNot { it.first == friendId }
            }
            .addOnFailureListener {
                // Handle failure (e.g., show a toast message)
            }
    }

    LaunchedEffect(Unit) {
        db.collection("friends")
            .get()
            .addOnSuccessListener { result ->
                val fetchedFriends = mutableListOf<Pair<String, Map<String, Any>>>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val additionalData = document.data - "name"
                    fetchedFriends.add(Pair(document.id, additionalData + ("name" to name)))
                }
                friendsWithIds = fetchedFriends
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Friends:",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        LazyColumn {
            items(friendsWithIds) { (id, data) ->
                val name = data["name"] as? String ?: ""

                // Using Card for each friend
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = name,
                            fontSize = 20.sp)
                        // Add more friend details here if needed
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { deleteFriend(id) }) {
                            Text("Delete Friend")
                        }
                    }
                }
            }
        }
    }
}
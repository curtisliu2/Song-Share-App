package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendAddScreen(navHostController: NavHostController) {
    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()

    // State variables for each attribute
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Form fields
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Friend's Name") })

        // Save button
        Button(onClick = {
            val newCharacter = hashMapOf(
                "name" to name,

            )
            db.collection("friends").add(newCharacter)
            navHostController.navigate("home")
        }) {
            Text("Save")
        }
    }
}
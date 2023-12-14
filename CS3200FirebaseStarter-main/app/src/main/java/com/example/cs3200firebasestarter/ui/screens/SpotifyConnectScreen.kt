package com.example.cs3200firebasestarter.ui.screens

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SpotifyConnectScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity
    val uri = activity?.intent?.data

    val clientId = "58762570f97643eeb21bd3c140f3e58f"
    val redirectUri = "com.songshare://callback"
    val scopes = "user-read-currently-playing" // Example scopes
    val responseType = "code"
    val spotifyAuthUrl = "https://accounts.spotify.com/authorize?client_id=$clientId&response_type=$responseType&redirect_uri=$redirectUri&scope=${Uri.encode(scopes)}"

    // Handle the OAuth callback
    LaunchedEffect(uri) {
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            val code = uri.getQueryParameter("code")
            // TODO: Exchange the code for an access token, and then proceed with your logic
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Spotify Connect:",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(spotifyAuthUrl))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally) // Centers the button horizontally
        ) {
            Text("Connect to Spotify")
        }
        Spacer(modifier = Modifier.height(400.dp))
    }
}

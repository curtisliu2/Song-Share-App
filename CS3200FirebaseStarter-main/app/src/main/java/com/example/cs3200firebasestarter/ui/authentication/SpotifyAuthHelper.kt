package com.example.cs3200firebasestarter.ui.authentication

import android.content.Context
import android.content.Intent
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class SpotifyAuthHelper(private val context: Context) {

    companion object {
        private const val AUTH_ENDPOINT = "https://accounts.spotify.com/authorize"
        private const val TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token"
        private const val CLIENT_ID = "58762570f97643eeb21bd3c140f3e58f"
        private const val REDIRECT_URI = "com.songshare://callback"
    }

    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse(AUTH_ENDPOINT),
        Uri.parse(TOKEN_ENDPOINT)
    )

    private val authService by lazy { AuthorizationService(context) }

    fun getAuthRequestIntent(): Intent {
        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(REDIRECT_URI)
        ).setScopes("streaming", "playlist-read-private")
            .build()

        return authService.getAuthorizationRequestIntent(authRequest)
    }


    fun dispose() {
        authService.dispose()
    }
}
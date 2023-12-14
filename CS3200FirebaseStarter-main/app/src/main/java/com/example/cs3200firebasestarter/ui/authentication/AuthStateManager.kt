package com.example.cs3200firebasestarter.ui.authentication
import android.content.Context
import android.content.SharedPreferences
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.TokenResponse

class AuthStateManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "AuthStatePreferences"
        private const val PREFS_KEY_AUTH_STATE = "authState"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var authState: AuthState
        get() {
            val stateJson = prefs.getString(PREFS_KEY_AUTH_STATE, null)
            return if (stateJson != null) {
                AuthState.jsonDeserialize(stateJson)
            } else {
                AuthState()
            }
        }
        set(value) {
            prefs.edit().putString(PREFS_KEY_AUTH_STATE, value.jsonSerializeString()).apply()
        }

    fun updateAfterAuthorization(response: AuthorizationResponse?, exception: AuthorizationException?) {
        val current = authState
        current.update(response, exception)
        authState = current
    }

    fun updateAfterTokenResponse(response: TokenResponse?, exception: AuthorizationException?) {
        val current = authState
        current.update(response, exception)
        authState = current
    }
}

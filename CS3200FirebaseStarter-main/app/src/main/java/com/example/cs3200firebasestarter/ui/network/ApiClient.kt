package com.example.cs3200firebasestarter.ui.network

import com.example.cs3200firebasestarter.ui.authentication.AuthStateManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.spotify.com/v1/"

    private fun createOkHttpClient(authStateManager: AuthStateManager): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val accessToken = authStateManager.authState.accessToken
            val authenticatedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(authenticatedRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    fun createSpotifyService(authStateManager: AuthStateManager): SpotifyService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(authStateManager))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(SpotifyService::class.java)
    }
}

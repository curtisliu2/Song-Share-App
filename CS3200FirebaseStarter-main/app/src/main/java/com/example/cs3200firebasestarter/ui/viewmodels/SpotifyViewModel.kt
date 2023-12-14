
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs3200firebasestarter.ui.repositories.SpotifyRepository
import com.example.cs3200firebasestarter.ui.repositories.TrackItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpotifyViewModel(private val spotifyRepository: SpotifyRepository) : ViewModel() {

    private var accessToken: String? = null

    init {
        viewModelScope.launch {
            accessToken = getAccessToken()
        }
    }

    private suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        // Construct the authorization header using android.util.Base64
        val authHeader = Base64.encodeToString("${spotifyRepository.clientId}:${spotifyRepository.clientSecret}".toByteArray(), Base64.NO_WRAP)

        val response = spotifyRepository.tokenService.getAccessToken("client_credentials", "Basic $authHeader").execute()
        return@withContext response.body()?.access_token
    }

    fun searchSpotify(query: String, onResult: (List<TrackItem>) -> Unit) {
        viewModelScope.launch {
            if (accessToken == null) {
                accessToken = getAccessToken()
            }
            accessToken?.let { token ->
                val response = spotifyRepository.spotifyService.searchSpotify(query, "track", "Bearer $token").execute()
                val results = response.body()?.tracks?.items ?: listOf()
                onResult(results)
            }
        }
    }
}

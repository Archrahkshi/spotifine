package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.SPOTIFY_CLIENT_ID
import com.archrahkshi.spotifine.util.SPOTIFY_REDIRECT_URI
import com.archrahkshi.spotifine.util.SPOTIFY_REQUEST_CODE
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AuthorizationClient.openLoginActivity(
            this,
            SPOTIFY_REQUEST_CODE,
            AuthorizationRequest.Builder(
                SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                SPOTIFY_REDIRECT_URI
            ).apply {
                setScopes(arrayOf("streaming", "user-library-read", "user-follow-read"))
            }.build()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SPOTIFY_REQUEST_CODE) {
            val response: AuthorizationResponse? =
                AuthorizationClient.getResponse(resultCode, intent)
            when (response?.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.i("Token", "OK")
                    startActivity(
                        Intent(this, LibraryActivity::class.java).apply {
                            putExtra(
                                ACCESS_TOKEN,
                                response.accessToken.also {
                                    Log.i("Access token", it)
                                }
                            )
                        }
                    )
                    finish()
                }
                AuthorizationResponse.Type.ERROR -> Log.wtf("Token", response.error)
                else -> Log.wtf("Token", "bullshit")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
    }
}

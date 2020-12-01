package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.ACCESS_TOKEN
import com.archrahkshi.spotifine.data.CLIENT_ID
import com.archrahkshi.spotifine.data.REDIRECT_URI
import com.archrahkshi.spotifine.data.REQUEST_CODE
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AuthorizationClient.openLoginActivity(
            this,
            REQUEST_CODE,
            AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
            ).apply {
                setScopes(arrayOf("streaming", "user-library-read", "user-follow-read"))
            }.build()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse? =
                AuthorizationClient.getResponse(resultCode, intent)
            val accessToken = response?.accessToken
            Log.i("Access token", accessToken.toString())
            when (response?.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.i("Token", "OK")
                    startActivity(Intent(this, LibraryActivity::class.java).apply {
                        putExtra(ACCESS_TOKEN, accessToken)
                    })
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

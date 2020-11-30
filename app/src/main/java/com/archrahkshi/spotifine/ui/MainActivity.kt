package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.ACCESS_TOKEN
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


var token = ""

class MainActivity : AppCompatActivity() {
    private val CLIENT_ID = "fbe0ec189f0247f99909e75530bac38e"
    private val REDIRECT_URI = "http://localhost:8888/callback/"
    private val REQUEST_CODE = 1337
    private var mSpotifyAppRemote: SpotifyAppRemote? = null


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
            token = accessToken.toString()
            Log.i("Access token", accessToken.toString())
            when (response?.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.i("Token", "OK")
                    startActivity(Intent(this, LibraryActivity::class.java).apply {
                        putExtra(ACCESS_TOKEN, accessToken)
                    })
                    //startActivity(Intent(this, TestActivity::class.java))
                }
                AuthorizationResponse.Type.ERROR -> Log.wtf("Token", response?.error)
                else -> Log.wtf("Token", "bullshit")
            }
        }
    }
    
    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
    }
}
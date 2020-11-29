package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

var token = ""
class MainActivity : AppCompatActivity() {
    private val CLIENT_ID = "fbe0ec189f0247f99909e75530bac38e"
    private val REDIRECT_URI = "http://localhost:8888/callback/"
    private val REQUEST_CODE = 1337
    //private val SCOPES = "user-library-read"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonProceed.setOnClickListener {
            val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            builder.setScopes(arrayOf("streaming"))
            val request:AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
         super.onActivityResult(requestCode, resultCode, intent)
         if (requestCode == REQUEST_CODE) {
             val response: AuthorizationResponse? = AuthorizationClient.getResponse(resultCode, intent)
             Log.d("aaaaaaaaaaaaaaaa", response?.accessToken.toString())
             token = response?.accessToken.toString()
             when (response?.type) {
                 AuthorizationResponse.Type.TOKEN -> {
                     Log.wtf("okay", "ok")
                     startActivity(Intent(this, MusicLibraryActivity::class.java))
                     //startActivity(Intent(this, TestActivity::class.java))
                 }
                 AuthorizationResponse.Type.ERROR -> {
                     Log.wtf("error", "not ok")
                 }
                 else -> {
                     Log.wtf("not error", "not ok")
                 }
             }
         }
     }


    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
    }
}
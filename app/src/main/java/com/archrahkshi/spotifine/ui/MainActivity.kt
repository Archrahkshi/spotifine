package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val CLIENT_ID = "fbe0ec189f0247f99909e75530bac38e"
    private val REDIRECT_URI = "http://localhost:8888/callback/"
    private val REQUEST_CODE = 1337
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Login Activity
        buttonProceed.setOnClickListener {

            // Set the connection parameters
           /* val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build()
            SpotifyAppRemote.connect(this, connectionParams,
                    object : Connector.ConnectionListener {
                        override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote
                            Log.d("MainActivity", "Connected! Yay!")

                            // Now you can start interacting with App Remote

                            connected()
                        }

                        override fun onFailure(throwable: Throwable) {
                            Log.e("MainActivity", throwable.message, throwable)

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    })*/


            val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

            builder.setScopes(arrayOf("streaming"))
            val request:AuthorizationRequest = builder.build()

            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)





        }
    }


 /*   private fun connected() {
        // Play a playlist
        mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        // information about track
        mSpotifyAppRemote!!.playerApi
                .subscribeToPlayerState()
                .setEventCallback { playerState: PlayerState ->
                    val track: Track? = playerState.track
                    if (track != null) {
                        Log.d("MainActivity", track.name.toString() + " by " + track.artist.name)
                    }
                }
        

    }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
         super.onActivityResult(requestCode, resultCode, intent)

         // Check if result comes from the correct activity
         if (requestCode == REQUEST_CODE) {
             val response: AuthorizationResponse? = AuthorizationClient.getResponse(resultCode, intent)
             Log.d("aaaaaaaaaaaaaaaa", response?.accessToken.toString())
             when (response?.type) {
                 AuthorizationResponse.Type.TOKEN -> {
                     Log.d("aaaaa2222222",  response.state?: "null")
                     startActivity(Intent(this, MusicLibraryActivity::class.java))
                 }
                 AuthorizationResponse.Type.ERROR -> {
                     Log.d("aaaaa2222222",  response.error?: "nullError")
                 }
                 else -> {
                     Log.d("aaaaaelse",  response?.state?: "nullElse")
                 }
             }
         }
     }

    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
        //SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }
}
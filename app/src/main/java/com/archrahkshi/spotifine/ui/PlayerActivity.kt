package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.ID
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class PlayerActivity : AppCompatActivity() {

    private val CLIENT_ID = "fbe0ec189f0247f99909e75530bac38e"
    private val REDIRECT_URI = "http://localhost:8888/callback/"
    private var spotifyAppRemote: SpotifyAppRemote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutPlayer,
            LyricsFragment(false)
        ).commit()

    }
    override fun onStart() {
        super.onStart()
        val id = intent.getStringExtra(ID)
        Log.wtf("id", id?.toString())
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@PlayerActivity.spotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                    this@PlayerActivity.spotifyAppRemote!!.playerApi.play("spotify:track:$id")
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

}
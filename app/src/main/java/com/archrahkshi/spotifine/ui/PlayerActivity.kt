package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.DURATION
import com.archrahkshi.spotifine.util.ID
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SPOTIFY_CLIENT_ID
import com.archrahkshi.spotifine.util.SPOTIFY_REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {
    private var pSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutPlayer,
            LyricsFragment(false).apply {
                arguments = Bundle().apply {
                    putString(NAME, intent.getStringExtra(NAME))
                    putString(ARTISTS, intent.getStringExtra(ARTISTS))
                }
            }
        ).commit()
    }

    override fun onStart() {
        super.onStart()
        val id = intent.getStringExtra(ID)
        val duration = intent.getLongExtra(DURATION, 0)
        Log.wtf("id", id?.toString())
        Log.wtf("duration", duration.toString())
        val connectionParams = ConnectionParams.Builder(SPOTIFY_CLIENT_ID)
            .setRedirectUri(SPOTIFY_REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(
            this,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@PlayerActivity.pSpotifyAppRemote = spotifyAppRemote
                    val appRemote = this@PlayerActivity.pSpotifyAppRemote!!
                    Log.d("PlayerActivity", "Connected! Yay!")

                    val seekBar = findViewById<SeekBar>(R.id.seekBar)
                    seekBar.max = duration.toInt()
                    var flag = 0
                    buttonPlay.text = getString(R.string.play)
                    seekBar.setOnSeekBarChangeListener(
                        object : OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                if (flag == 0) {
                                    seekBar.progress = 0
                                } else {
                                    appRemote.playerApi.seekTo(seekBar.progress.toLong())
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar) {
                                if (flag == 1) {
                                    appRemote.playerApi.pause()
                                }
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                                if (flag == 1) {
                                    appRemote.playerApi.resume()
                                }
                            }
                        }
                    )
                    buttonPlay.setOnClickListener {
                        when (flag) {
                            0 -> {
                                appRemote.playerApi.play("spotify:track:$id")
                                flag = 1
                                buttonPlay.text = getString(R.string.pause)
                            }
                            1 -> {
                                appRemote.playerApi.pause()
                                flag = 2
                                buttonPlay.text = getString(R.string.play)
                            }
                            2 -> {
                                appRemote.playerApi.resume()
                                flag = 1
                                buttonPlay.text = getString(R.string.pause)
                            }
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(pSpotifyAppRemote)
    }
}

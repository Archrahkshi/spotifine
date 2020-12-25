package com.archrahkshi.spotifine.ui.player

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.player.lyricsFragment.LyricsFragment
import com.archrahkshi.spotifine.util.*
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*
import timber.log.Timber

class PlayerActivity : AppCompatActivity() {
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutPlayer,
                LyricsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARTISTS, intent.getStringExtra(ARTISTS))
                        putString(NAME, intent.getStringExtra(NAME))
                        putBoolean(IS_LYRICS_TRANSLATED, false)
                    }
                }
            ).commit()
    }

    override fun onStart() {
        super.onStart()

        val id = intent.getStringExtra(ID)
        val duration = intent.getLongExtra(DURATION, 0)
        Timber.wtf(id?.toString())
        Timber.wtf(duration.toString())

        try {
            SpotifyAppRemote.connect(
                this,
                ConnectionParams.Builder(SPOTIFY_CLIENT_ID)
                    .setRedirectUri(SPOTIFY_REDIRECT_URI)
                    .showAuthView(true)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        this@PlayerActivity.spotifyAppRemote = spotifyAppRemote
                        Timber.d("Connected! Yay!")

                        val seekBar = findViewById<SeekBar>(R.id.seekBar)
                        seekBar.max = duration.toInt()
                        var flag = 0
                        //buttonPlay.text = getString(R.string.play)
                        seekBar.setOnSeekBarChangeListener(
                            object : OnSeekBarChangeListener {
                                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                    if (flag == 0) {
                                        seekBar.progress = 0
                                    } else {
                                        spotifyAppRemote.playerApi.seekTo(seekBar.progress.toLong())
                                    }
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar) {
                                    if (flag == 1) {
                                        spotifyAppRemote.playerApi.pause()
                                    }
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar) {
                                    if (flag == 1) {
                                        spotifyAppRemote.playerApi.resume()
                                    }
                                }
                            }
                        )
                        buttonPlay.setOnClickListener {
                            when (flag) {
                                0 -> {
                                    spotifyAppRemote.playerApi.play("spotify:track:$id")
                                    flag = 1
                                    //buttonPlay.text = getString(R.string.pause)
                                }
                                1 -> {
                                    spotifyAppRemote.playerApi.pause()
                                    flag = 2
                                    //buttonPlay.text = getString(R.string.play)
                                }
                                2 -> {
                                    spotifyAppRemote.playerApi.resume()
                                    flag = 1
                                    //buttonPlay.text = getString(R.string.pause)
                                }
                            }
                        }
                    }

                    override fun onFailure(throwable: Throwable) {
                        Timber.e(throwable)

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }
}

package com.archrahkshi.spotifine.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.DURATION
import com.archrahkshi.spotifine.data.ID
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    private val CLIENT_ID = "fbe0ec189f0247f99909e75530bac38e"
    private val REDIRECT_URI = "http://localhost:8888/callback/"
    private var pSpotifyAppRemote: SpotifyAppRemote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutPlayer,
            LyricsFragment(false)
        ).commit()

    }
    override fun onStart() { // TODO: Когда-нибудь реализовать активный ползунок
        super.onStart()
        val id = intent.getStringExtra(ID)
        val duration = intent.getLongExtra(DURATION, 0)
        Log.wtf("id", id?.toString())
        Log.wtf("duration", duration.toString())
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                @SuppressLint("SetTextI18n")
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@PlayerActivity.pSpotifyAppRemote = spotifyAppRemote
                    val appRemote = this@PlayerActivity.pSpotifyAppRemote!!
                    Log.d("MainActivity", "Connected! Yay!")

                    val seekBar = findViewById<SeekBar>(R.id.seekBar)
                    seekBar.max = duration.toInt()
                    var flag = 0
                    buttonPlay.text = "PLAY"
                    seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                            if (flag == 0) {      // TODO: Убрать этот кривой костыль
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
                    })
                    buttonPlay.setOnClickListener {
                        when (flag) {
                            0 -> {
                                appRemote.playerApi.play("spotify:track:$id")
                                flag = 1
                                buttonPlay.text = "PAUSE"
                            }
                            1 -> {
                                appRemote.playerApi.pause()
                                flag = 2
                                buttonPlay.text = "PLAY"
                            }
                            2 -> {
                                appRemote.playerApi.resume()
                                flag = 1
                                buttonPlay.text = "PAUSE"
                            }
                        }
                    }

                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }
    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(pSpotifyAppRemote)
    }

}
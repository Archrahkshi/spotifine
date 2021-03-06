package com.archrahkshi.spotifine.ui.player

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.TrackDataProviderFactory
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode
import com.archrahkshi.spotifine.ui.commonViews.presenters.FullscreenModePresenter
import com.archrahkshi.spotifine.ui.player.lyricsFragment.LyricsFragment
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.DURATION
import com.archrahkshi.spotifine.util.ID
import com.archrahkshi.spotifine.util.IS_LYRICS_TRANSLATED
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SPOTIFY_CLIENT_ID
import com.archrahkshi.spotifine.util.SPOTIFY_REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*
import timber.log.Timber

class PlayerActivity : AppCompatActivity(), IFullscreenMode {
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val fullscreenModePresenter by lazy { FullscreenModePresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreenModePresenter.setSelectionFullscreenMode()
        setContentView(R.layout.activity_player)
        TrackDataProviderFactory.provide()

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutPlayer,
                LyricsFragment().apply {
                    arguments = Bundle().apply {
                        putString(
                            ARTISTS,
                            intent.getStringExtra(ARTISTS).also {
                                TrackDataProviderFactory.instance!!.setArtists(it!!)
                            }
                        )
                        putString(
                            NAME,
                            intent.getStringExtra(NAME).also {
                                TrackDataProviderFactory.instance!!.setName(it!!)
                            }
                        )
                        putBoolean(IS_LYRICS_TRANSLATED, false)
                    }
                }
            ).commit()
    }

    override fun onStart() {
        super.onStart()

        SpotifyAppRemote.connect(
            this,
            ConnectionParams.Builder(SPOTIFY_CLIENT_ID)
                .setRedirectUri(SPOTIFY_REDIRECT_URI)
                .showAuthView(true)
                .build(),
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@PlayerActivity.spotifyAppRemote = spotifyAppRemote

                    seekBar.max = intent.getLongExtra(DURATION, 0).toInt()
                    var flag = 0
                    // buttonPlay.text = getString(R.string.play)
                    seekBar.setOnSeekBarChangeListener(
                        object : OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar,
                                i: Int,
                                b: Boolean
                            ) {
                                if (flag == 0)
                                    seekBar.progress = 0
                                else
                                    spotifyAppRemote.playerApi.seekTo(seekBar.progress.toLong())
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar) {
                                if (flag == 1)
                                    spotifyAppRemote.playerApi.pause()
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                                if (flag == 1)
                                    spotifyAppRemote.playerApi.resume()
                            }
                        }
                    )
                    buttonPlay.setOnClickListener {
                        when (flag) {
                            0 -> {
                                spotifyAppRemote.playerApi
                                    .play("spotify:track:${intent.getStringExtra(ID)}")
                                flag = 1
                                buttonPlay.setBackgroundResource(R.drawable.back_pause_btn)
                            }
                            1 -> {
                                spotifyAppRemote.playerApi.pause()
                                flag = 2
                                buttonPlay.setBackgroundResource(R.drawable.back_play_btn)
                            }
                            2 -> {
                                spotifyAppRemote.playerApi.resume()
                                flag = 1
                                buttonPlay.setBackgroundResource(R.drawable.back_pause_btn)
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
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    /**
     * Fullscreen mode implementation
     */

    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        setTheme(if (isFullscreenModeSelected) R.style.fullscreen else R.style.spotifine)
    }
}

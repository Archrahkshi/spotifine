package com.archrahkshi.spotifine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import kotlinx.android.synthetic.main.activity_music_library.*


enum class ListTypes {PLAYLISTS, ARTISTS, ALBUMS}

class MusicLibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_library)

        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            PlaylistsFragment(ListTypes.PLAYLISTS)
        ).commit()

        buttonPlaylists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                PlaylistsFragment(ListTypes.PLAYLISTS)
            )
        }

        buttonArtists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                PlaylistsFragment(ListTypes.ARTISTS)
            )
        }

        buttonAlbums.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                PlaylistsFragment(ListTypes.ALBUMS)
            )
        }
    }
}
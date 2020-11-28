package com.archrahkshi.spotifine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.Playlist
import kotlinx.android.synthetic.main.activity_music_library.*

class MusicLibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_library)

        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            LibraryListsFragment<Playlist>()
        ).commit()

        buttonPlaylists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Playlist>()
            ).commit()
        }

        buttonArtists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Artist>()
            ).commit()
        }

        buttonAlbums.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Album>()
            ).commit()
        }
    }
}
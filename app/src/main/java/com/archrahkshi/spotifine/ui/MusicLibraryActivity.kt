package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.URL
import kotlinx.android.synthetic.main.activity_music_library.*

class MusicLibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_library)

       var flag = "playlist"

        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            LibraryListsFragment<Playlist>().apply { arguments = Bundle().apply {
                putString("flag", flag)
                Log.wtf("flag0", flag)
            } }
        ).commit()

        buttonPlaylists.setOnClickListener {
            flag = "playlist"
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Playlist>().apply { arguments = Bundle().apply { putString("flag", flag) } }
            ).commit()
        }

        buttonArtists.setOnClickListener {
            flag = "artist"
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Artist>().apply { arguments = Bundle().apply { putString("flag", flag) } }
            ).commit()
        }

        buttonAlbums.setOnClickListener {
            flag = "album"
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment<Album>().apply { arguments = Bundle().apply { putString("flag", flag) } }
            ).commit()
        }
    }
}
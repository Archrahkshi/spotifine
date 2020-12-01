package com.archrahkshi.spotifine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.ACCESS_TOKEN
import com.archrahkshi.spotifine.data.ALBUMS
import com.archrahkshi.spotifine.data.ARTISTS
import com.archrahkshi.spotifine.data.LIST_TYPE
import com.archrahkshi.spotifine.data.PLAYLISTS
import kotlinx.android.synthetic.main.activity_library.buttonAlbums
import kotlinx.android.synthetic.main.activity_library.buttonArtists
import kotlinx.android.synthetic.main.activity_library.buttonPlaylists

class LibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        replaceFragmentWith(PLAYLISTS)

        buttonPlaylists.setOnClickListener {
            replaceFragmentWith(PLAYLISTS)
        }

        buttonArtists.setOnClickListener {
            replaceFragmentWith(ARTISTS)
        }

        buttonAlbums.setOnClickListener {
            replaceFragmentWith(ALBUMS)
        }
    }

    private fun replaceFragmentWith(listType: String) {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            LibraryListsFragment().apply {
                arguments = Bundle().apply {
                    putString(LIST_TYPE, listType)
                    putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                }
            }
        ).commit()
    }
}

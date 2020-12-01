package com.archrahkshi.spotifine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.*
import kotlinx.android.synthetic.main.activity_library.*

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
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
        
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            LibraryListsFragment().apply {
                arguments = Bundle().apply {
                    putString(LIST_TYPE, PLAYLISTS)
                    putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                }
            }
        ).commit()
        
        buttonPlaylists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment().apply {
                    arguments = Bundle().apply {
                        putString(LIST_TYPE, PLAYLISTS)
                        putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                    }
                }
            ).commit()
        }
        
        buttonArtists.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment().apply {
                    arguments = Bundle().apply {
                        putString(LIST_TYPE, ARTISTS)
                        putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                    }
                }
            ).commit()
        }
        
        buttonAlbums.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutLibrary,
                LibraryListsFragment().apply {
                    arguments = Bundle().apply {
                        putString(LIST_TYPE, ALBUMS)
                        putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                    }
                }
            ).commit()
        }
    }
}
package com.archrahkshi.spotifine.ui.library

import android.app.Instrumentation
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.TrackDataProviderFactory
import com.archrahkshi.spotifine.data.factories.TracksListProviderFactory
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.LibraryListsFragment
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.INDEX_0
import com.archrahkshi.spotifine.util.INDEX_1
import com.archrahkshi.spotifine.util.INDEX_2
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.PLAYLISTS
import kotlinx.android.synthetic.main.activity_library.navigationView
import kotlinx.android.synthetic.main.toolbar.btnBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class LibraryActivity : AppCompatActivity() {
    companion object {
        const val KEY_CURRENT_FRAGMENT = "Current fragment"
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(
            KEY_CURRENT_FRAGMENT,
            when (navigationView.selectedItemId) {
                R.id.item_playlists -> INDEX_0
                R.id.item_artists -> INDEX_1
                else -> INDEX_2
            }
        )
        super.onSaveInstanceState(outState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        TrackDataProviderFactory.provide()


        if (savedInstanceState == null) {
            replaceFragmentWith(PLAYLISTS)
        } else {
            replaceFragmentWith(
                when (savedInstanceState.getInt(KEY_CURRENT_FRAGMENT)) {
                    INDEX_0 -> PLAYLISTS
                    INDEX_1 -> ARTISTS
                    else -> ALBUMS
            })
        }

        btnBack.setOnClickListener {
            CoroutineScope(Default).launch {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }
        }

        navigationView.setOnNavigationItemSelectedListener {
            when (it.title) {
                getString(R.string.library_artists) -> replaceFragmentWith(ARTISTS)
                getString(R.string.library_albums) -> replaceFragmentWith(ALBUMS)
                getString(R.string.library_playlists) -> replaceFragmentWith(PLAYLISTS)
            }
            true
        }
    }

    private fun replaceFragmentWith(listType: String) {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutLibrary,
            LibraryListsFragment().apply {
                arguments = Bundle().apply {
                    putString(LIST_TYPE, listType)
                    putString(
                        ACCESS_TOKEN,
                        intent.getStringExtra(ACCESS_TOKEN).also {
                            TrackDataProviderFactory.instance!!.setAccessToken(it)
                        }
                    )
                }
            }
        ).setTransition(TRANSIT_FRAGMENT_FADE).commit()
    }
}

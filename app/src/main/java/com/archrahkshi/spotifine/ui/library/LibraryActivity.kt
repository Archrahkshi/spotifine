package com.archrahkshi.spotifine.ui.library

import android.app.Instrumentation
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.LibraryListsFragment
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.PLAYLISTS
import kotlinx.android.synthetic.main.activity_library.navigationView
import kotlinx.android.synthetic.main.toolbar.imgBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        if (savedInstanceState == null)
            replaceFragmentWith(PLAYLISTS)

        imgBack.setOnClickListener {
            CoroutineScope(Default).launch {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }
        }

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.title) {
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
                    putString(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                }
            }
        ).setTransition(TRANSIT_FRAGMENT_FADE).commit()
    }
}

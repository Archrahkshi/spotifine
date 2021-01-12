package com.archrahkshi.spotifine.ui.library

import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.TrackDataProviderFactory
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode
import com.archrahkshi.spotifine.ui.commonViews.presenters.FullscreenModePresenter
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.LibraryListsFragment
import com.archrahkshi.spotifine.ui.settings.SettingsActivity
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.PLAYLISTS
import kotlinx.android.synthetic.main.activity_library.navigationView
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.imageViewSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class LibraryActivity : AppCompatActivity(), IFullscreenMode {

    private val fullscreenModePresenter by lazy { FullscreenModePresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreenModePresenter.setSelectionFullscreenMode()
        setContentView(R.layout.activity_library)
        TrackDataProviderFactory.provide()

        fullscreenModePresenter.setSelectionFullscreenMode()

        if (savedInstanceState == null)
            replaceFragmentWith(PLAYLISTS)

        imageViewSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        imageViewBack.setOnClickListener {
            CoroutineScope(Default).launch {
                Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
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
                            TrackDataProviderFactory.instance!!.setAccessToken(it!!)
                        }
                    )
                }
            }
        ).setTransition(TRANSIT_FRAGMENT_FADE).commit()
    }

    /**
     * Fullscreen mode implementation
     */

    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        setTheme(if (isFullscreenModeSelected) R.style.fullscreen else R.style.spotifine)
    }
}

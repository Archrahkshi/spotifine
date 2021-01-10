package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.UserPreferencesFactory
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode
import com.archrahkshi.spotifine.ui.commonViews.presenters.FullscreenModePresenter
import com.archrahkshi.spotifine.ui.library.LibraryActivity
import com.archrahkshi.spotifine.ui.settings.views.IFullscreenModeCheckbox
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.SPOTIFY_CLIENT_ID
import com.archrahkshi.spotifine.util.SPOTIFY_REDIRECT_URI
import com.archrahkshi.spotifine.util.SPOTIFY_REQUEST_CODE
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import timber.log.Timber

class MainActivity : AppCompatActivity(), IFullscreenMode {
    private val fullscreenModePresenter by lazy { FullscreenModePresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserPreferencesFactory.provide(applicationContext)
        fullscreenModePresenter.setSelectionFullscreenMode()
        setContentView(R.layout.activity_main)

        AuthorizationClient.openLoginActivity(
            this,
            SPOTIFY_REQUEST_CODE,
            AuthorizationRequest.Builder(
                SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                SPOTIFY_REDIRECT_URI
            ).apply {
                setScopes(arrayOf("streaming", "user-library-read", "user-follow-read"))
            }.build()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SPOTIFY_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response?.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    startActivity(
                        Intent(this, LibraryActivity::class.java).apply {
                            putExtra(ACCESS_TOKEN, response.accessToken.also { Timber.i(it) })
                        }
                    )
                    finish()
                }
                AuthorizationResponse.Type.ERROR -> Timber.wtf(response.error)
                else -> Timber.wtf("bullshit")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
    }

    /**
     * Fullscreen mode implementation
     */

    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        if (isFullscreenModeSelected) {
            Log.i("setiing", "!")
            setTheme(R.style.fullscreen)
        } else {
            setTheme(R.style.spotifine)
        }
    }
}

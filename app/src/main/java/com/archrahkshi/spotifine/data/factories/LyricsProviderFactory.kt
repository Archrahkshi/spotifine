package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providersImpls.LyricsProviderImpl
import com.archrahkshi.spotifine.data.providersImpls.TracksListProviderImpl
import com.archrahkshi.spotifine.ui.player.lyricsFragment.LyricsFragment

class LyricsProviderFactory {
    companion object {
        var instance: LyricsProviderImpl? = null
        fun provide(fragment: LyricsFragment) = LyricsProviderImpl(fragment).also {
            instance = it
        }
    }
}
package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.LyricsProviderImpl
import com.archrahkshi.spotifine.ui.player.lyricsFragment.LyricsFragment

class LyricsProviderFactory private constructor() {
    companion object {
        var instance: LyricsProviderImpl? = null
        fun provide(fragment: LyricsFragment) = LyricsProviderImpl(fragment).also {
            instance = it
        }
    }
}

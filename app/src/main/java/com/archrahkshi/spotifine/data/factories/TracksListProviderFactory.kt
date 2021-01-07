package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.TracksListProviderImpl

class TracksListProviderFactory {
    companion object {
        var instance: TracksListProviderImpl? = null
        fun provide() = TracksListProviderImpl().also {
            instance = it
        }
    }
}

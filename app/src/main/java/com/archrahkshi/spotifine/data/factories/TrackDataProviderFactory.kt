package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.TrackDataProviderImpl

class TrackDataProviderFactory {
    companion object {
        var instance: TrackDataProviderImpl? = null
        fun provide() = TrackDataProviderImpl().also {
            instance = it
        }
    }
}

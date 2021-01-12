package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.TrackDataProviderImpl

class TrackDataProviderFactory private constructor() {
    companion object {
        var instance: TrackDataProviderImpl? = null
        fun provide() = TrackDataProviderImpl().also {
            instance = it
        }
    }
}

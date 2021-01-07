package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.LibraryListProviderImpl

class LibraryListProviderFactory {
    companion object {
        var instance: LibraryListProviderImpl? = null
        fun provide() = LibraryListProviderImpl().also {
            instance = it
        }
    }
}

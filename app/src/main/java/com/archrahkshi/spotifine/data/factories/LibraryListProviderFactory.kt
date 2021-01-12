package com.archrahkshi.spotifine.data.factories

import com.archrahkshi.spotifine.data.providerImpls.LibraryListProviderImpl
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.LibraryListsFragment

class LibraryListProviderFactory private constructor() {
    companion object {
        var instance: LibraryListProviderImpl? = null
        fun provide(fragment: LibraryListsFragment) = LibraryListProviderImpl(fragment).also {
            instance = it
        }
    }
}

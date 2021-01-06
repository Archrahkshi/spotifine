package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters

import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.data.factories.LibraryListProviderFactory
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.ITracksList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LibraryListPresenter(private val viewState: ITracksList): Provider {
    override val provider = LibraryListProviderFactory.instance!!

    fun setupList(url: String, listType: String, token: String) {
        CoroutineScope(IO).launch {
            val list = provider.getList(url, listType, token)
            viewState.setupList(list)
        }
    }
}
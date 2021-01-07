package com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters

import com.archrahkshi.spotifine.data.factories.TracksListProviderFactory
import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.ITracksList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TracksListPresenter(private val viewState: ITracksList) : Provider {
    override val provider = TracksListProviderFactory.instance!!

    fun setupList(url: String, token: String?) {
        CoroutineScope(IO).launch {
            val list = provider.getList(url, token)
            viewState.setupList(list)
        }
    }
}

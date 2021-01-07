package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views

import com.archrahkshi.spotifine.data.ListType

interface ITracksList {
    suspend fun setupList(list: List<ListType>)
}

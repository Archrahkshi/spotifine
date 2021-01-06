package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views

import com.archrahkshi.spotifine.data.ListType
import com.archrahkshi.spotifine.data.Track

interface ITracksList {
    suspend fun setupList(list: List<ListType>)
}

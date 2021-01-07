package com.archrahkshi.spotifine.ui.library.tracksFragment.views

import com.archrahkshi.spotifine.data.Track

interface ITracksList {
    suspend fun setupList(list: List<Track>)
}

package com.archrahkshi.spotifine.data.providers

import com.archrahkshi.spotifine.data.Track

interface ITracksListProvider: BaseProvider {
    suspend fun getList(url: String, accessToken: String?): List<Track>
}
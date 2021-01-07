package com.archrahkshi.spotifine.data.providerImpls

import com.archrahkshi.spotifine.data.providers.ITracksListProvider
import com.archrahkshi.spotifine.util.ALBUM_FROM_PLAYLIST_DISTINCTION
import com.archrahkshi.spotifine.util.SPOTIFY_PREFIX
import com.archrahkshi.spotifine.util.asTrack
import com.archrahkshi.spotifine.util.getJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TracksListProviderImpl : ITracksListProvider {
    // TODO: заменить на GSON
    override suspend fun getList(url: String, accessToken: String?) = withContext(Dispatchers.IO) {
        Timber.i(accessToken ?: "null")
        val json = url.getJson(accessToken)
        val items = json["items"].asJsonArray
        when (
            json["href"]
                .asString
                .removePrefix(SPOTIFY_PREFIX)
                .take(ALBUM_FROM_PLAYLIST_DISTINCTION)
        ) {
            "album" -> items.map { it.asJsonObject.asTrack() }
            "playl" -> items.map { it.asJsonObject["track"].asJsonObject.asTrack() }
            else -> listOf()
        }
    }
}

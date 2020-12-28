package com.archrahkshi.spotifine.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun createTrackLists(url: String, accessToken: String?) = withContext(Dispatchers.IO) {
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

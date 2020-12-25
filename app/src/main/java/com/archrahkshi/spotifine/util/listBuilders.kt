package com.archrahkshi.spotifine.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun createTrackLists(
        url: String,
        accessToken: String?
) = withContext(Dispatchers.IO) {
    val json = getJsonFromApi(url, accessToken)
    println(json.toString())
    val items = json["items"].asJsonArray
    when (
        json["href"]
                .asString
                .removePrefix("https://api.spotify.com/v1/")
                .take(ALBUM_FROM_PLAYLIST_DISTINCTION)
        ) {
        "album" -> items.map { createTrack(it.asJsonObject) }
        "playl" -> items.map { createTrack(it.asJsonObject["track"].asJsonObject) }
        else -> listOf()
    }
}

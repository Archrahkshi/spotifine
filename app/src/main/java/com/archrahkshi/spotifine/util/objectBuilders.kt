package com.archrahkshi.spotifine.util

import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.Track
import com.google.gson.JsonObject

fun createPlaylist(item: JsonObject): Playlist {
    val tracks = item["tracks"].asJsonObject
    return Playlist(
        image = item["images"].asJsonArray.first().asJsonObject["url"].asString,
        name = item["name"].asString,
        size = tracks["total"].asInt,
        url = tracks["href"].asString
    )
}

fun createArtist(item: JsonObject) = Artist(
    image = item["images"].asJsonArray[1].asJsonObject["url"].asString,
    name = item["name"].asString,
    url = "artists/${item["id"].asString}"
)

fun createAlbum(item: JsonObject, type: String) = Album(
    artists = item["artists"].asJsonArray.joinToString { it.asJsonObject["name"].asString },
    image = item["images"].asJsonArray[1].asJsonObject["url"].asString,
    name = item["name"].asString,
    size = item["total_tracks"].asInt,
    url = if (type == FROM_ARTIST)
        "${item["href"].asString}/tracks"
    else
        item["tracks"].asJsonObject["href"].asString
)

fun createTrack(item: JsonObject) = Track(
    name = item["name"].asString,
    artists = item["artists"].asJsonArray
        .joinToString { it.asJsonObject["name"].asString },
    duration = item["duration_ms"].asLong,
    id = item["id"].asString
)

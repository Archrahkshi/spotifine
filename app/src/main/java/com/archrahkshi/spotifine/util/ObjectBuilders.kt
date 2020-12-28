package com.archrahkshi.spotifine.util

import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.Track
import com.google.gson.JsonObject

fun JsonObject.asPlaylist(): Playlist {
    val tracks = this["tracks"].asJsonObject
    return Playlist(
        image = this["images"].asJsonArray.first().asJsonObject["url"].asString,
        name = this["name"].asString,
        size = tracks["total"].asInt,
        url = tracks["href"].asString
    )
}

fun JsonObject.asArtist() = Artist(
    image = this["images"].asJsonArray[1].asJsonObject["url"].asString,
    name = this["name"].asString,
    url = "artists/${this["id"].asString}"
)

fun JsonObject.asAlbum(type: String) = Album(
    artists = this["artists"].asJsonArray.joinToString { it.asJsonObject["name"].asString },
    image = this["images"].asJsonArray[1].asJsonObject["url"].asString,
    name = this["name"].asString,
    size = this["total_tracks"].asInt,
    url = if (type == FROM_ARTIST)
        "${this["href"].asString}/tracks"
    else
        this["tracks"].asJsonObject["href"].asString
)

fun JsonObject.asTrack() = Track(
    name = this["name"].asString,
    artists = this["artists"].asJsonArray
        .joinToString { it.asJsonObject["name"].asString },
    duration = this["duration_ms"].asLong,
    id = this["id"].asString
)
